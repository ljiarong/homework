package cn.edu.xmu.oomall.shop.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.shop.controller.vo.TemplateModifyVo;
import cn.edu.xmu.oomall.shop.controller.vo.TemplateVo;
import cn.edu.xmu.oomall.shop.dao.FreightTemplateDao;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import cn.edu.xmu.oomall.shop.service.dto.TemplateRetDto;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.*;

@Service
public class TemplateService {
    private final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    private  final Integer TEMPLATENAMEMAXLEN=128;
    private RedisUtil redisUtil;
    private FreightTemplateDao freightTemplateDao;

    private RegionTemplateDao regionTemplateDao;

    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    public TemplateService(FreightTemplateDao freightTemplateDao,
                           RegionTemplateDao regionTemplateDao,
                           RedisUtil redisUtil,
                           RocketMQTemplate rocketMQTemplate
    ){
        this.freightTemplateDao = freightTemplateDao;
        this.regionTemplateDao=regionTemplateDao;
        this.redisUtil=redisUtil;
        this.rocketMQTemplate=rocketMQTemplate;
    }

    /**
     * ???????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 7:42
     * @param shopId ??????id
     * @param name ????????????
     * @param page
     * @param pageSize
     */
    public PageDto<TemplateVo> retrieveTemplateByName(Long shopId, String name, Integer page,Integer pageSize){
        List<Template> ret= freightTemplateDao.retrieveTemplateByName(shopId,name,page,pageSize);
        List<TemplateVo> voList=ret.stream().map(bo->TemplateVo.builder().id(bo.getId()).name(bo.getName()).defaultModel(bo.getDefaultModel()!=0).build())
                                            .collect(Collectors.toList());
        PageDto<TemplateVo> pageObj=new PageDto<>(voList,page,pageSize);
        return pageObj;
    }

    /**
     * ???????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 7:45
     * @param shopId
     * @param vo
     * @param user
     */

    @Transactional
    public TemplateVo createTemplate(Long shopId, TemplateModifyVo vo, UserDto user){
        Template template=Template.builder().shopId(shopId).name(vo.getName()).defaultModel(vo.getDefaultModel()).build();
        template=freightTemplateDao.insertTemplate(template,user);
        return TemplateVo.builder().id(template.getId()).name(template.getName()).defaultModel(template.getDefaultModel()!=0).build();
    }

    /**
     * ???????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 7:46
     * @param id ??????id
     * @param user
     */
    @Transactional
    public TemplateVo cloneTemplate(Long id,Long shopId,UserDto user)throws  CloneNotSupportedException{
        Template template= freightTemplateDao.findTemplateById(id);
        if(shopId!=PLATFORM&&shopId!=template.getShopId())
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));

        logger.debug("cloneTemplate: oldTmp={}",template);
        /*
            ?????????????????????????????????????????????
            ?????????????????????
            ???????????????Name????????????????????????????????????????????????
            ????????????????????????????????????????????????
         */
        StringBuilder sb=new StringBuilder(TEMPLATENAMEMAXLEN);
        int rand=(int)(Math.random()*(Integer.MAX_VALUE));
        sb.append(template.getName());
        sb.append(rand);
        String name=sb.toString();
        if(name.length()>TEMPLATENAMEMAXLEN)
            name=name.subSequence(0,TEMPLATENAMEMAXLEN).toString();

        Template newTmp=Template.builder().shopId(template.getShopId()).name(name).defaultModel(Template.COMMON).build();
        logger.debug("cloneTemplate: newTmp={}",newTmp);
        newTmp=freightTemplateDao.insertTemplate(newTmp,user);
        logger.debug("cloneTemplate: newTmpName={}",newTmp.getName());
        return TemplateVo.builder().id(newTmp.getId()).name(newTmp.getName()).defaultModel(newTmp.getDefaultModel()!=0).build();
    }

    /**
     * ????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 7:57
     * @param shopId
     * @param id
     */
    public TemplateRetDto findTemplateById(Long shopId,Long id){
        Template tmp= freightTemplateDao.findTemplateById(id);
        if(shopId!=PLATFORM&&shopId!=tmp.getShopId())
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));

        UserDto creator=new UserDto();
        creator.setName(tmp.getCreatorName());
        creator.setId(tmp.getCreatorId());
        UserDto modifier=new UserDto();
        modifier.setName(tmp.getModifierName());
        modifier.setId(tmp.getModifierId());
        TemplateRetDto ret=TemplateRetDto.builder().id(tmp.getId()).name(tmp.getName()).defaultModel(tmp.getDefaultModel())
                .gmtCreate(tmp.getGmtCreate()).gmtModified(tmp.getGmtModified()).creator(creator).modifier(modifier).build();
        logger.debug("findTemplateById: templateRetDto={}",ret);
        return ret;
    }

    /**
     * ???????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 7:58
     * @param shopId
     * @param id
     * @param vo
     * @param user
     */
    @Transactional
    public void updateTemplateById(Long shopId,Long id,TemplateModifyVo vo,UserDto user){
        logger.debug("updateTemplateById: shopId={},id={},vo={},user={}",shopId,id,vo,user);
        Template template=freightTemplateDao.findTemplateById(id);
        if(shopId!=PLATFORM&&shopId!=template.getShopId())
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));

        logger.debug("updateTemplateById: oldTemplate={}",template);
        //??????????????????????????????????????????????????????????????????
        if(Objects.equals(vo.getDefaultModel(), Template.DEFAULT)){
            Optional<Template> defaultTemplate=freightTemplateDao.retrieveTemplateByShopIdAndDefaultModel(shopId);
            if(null!=defaultTemplate.orElse(null)){
                logger.debug("updateTemplateById: defaultTemplate={}",defaultTemplate.get());
                defaultTemplate.get().setDefaultModel(Template.COMMON);
                freightTemplateDao.saveTemplateById(defaultTemplate.get(),user);
                logger.debug("updateTemplateById: defaultTemplate has changed from 1 to {}",defaultTemplate.get().getDefaultModel());
            }
        }
        template.setName(vo.getName());
        template.setDefaultModel(vo.getDefaultModel());
        freightTemplateDao.saveTemplateById(template,user);
    }

    /**
     * ??????????????????????????????????????????????????????
     * @author Zhanyu Liu
     * @date 2022/11/30 8:03
     * @param id
     * @param shopId
     */
    public void sendDelTemplateMsg(UserDto user,Long shopId,Long id){
        Template template=freightTemplateDao.findTemplateById(id);
        if(shopId!=PLATFORM&&shopId!=template.getShopId())
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "??????", id, shopId));
        /*
            ???goods?????????Transaction????????????????????????????????????TemplateId???????????????
         */
        Message msg = MessageBuilder.withPayload(id.toString()).setHeader("user", user).build();
        rocketMQTemplate.sendMessageInTransaction("Del-Template", msg, null);
    }

    @Transactional
    public void deleteTemplate(Long id){
        List<String> delKeys = new ArrayList<>(regionTemplateDao.deleteTemplate(id));
        delKeys.forEach(key->{if(redisUtil.hasKey(key))redisUtil.del(key);});
    }
}
