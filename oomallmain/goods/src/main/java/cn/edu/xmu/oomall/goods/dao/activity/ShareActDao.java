//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao.activity;

import cn.edu.xmu.oomall.goods.dao.bo.Activity;
import cn.edu.xmu.oomall.goods.mapper.ShareActPoMapper;
import cn.edu.xmu.oomall.goods.mapper.po.ActivityPo;
import cn.edu.xmu.oomall.goods.mapper.po.ShareActPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;
import static cn.edu.xmu.javaee.core.util.Common.copyObj;

@Repository
public class ShareActDao implements ActivityInf{

    private Logger logger = LoggerFactory.getLogger(ShareActDao.class);

    private ShareActPoMapper actPoMapper;

    @Autowired
    public ShareActDao(ShareActPoMapper actPoMapper) {
        this.actPoMapper = actPoMapper;
    }

    @Override
    public Activity getActivity(ActivityPo po) throws RuntimeException {
        Activity bo = cloneObj(po, Activity.class);
        Optional<ShareActPo> ret = this.actPoMapper.findById(po.getObjectId());
        ret.ifPresent(actPo -> {
            copyObj(actPo, bo);
        } );
        return bo;
    }

    @Override
    public String insert(Activity bo) throws RuntimeException{
        ShareActPo po = cloneObj(bo, ShareActPo.class);
        ShareActPo newPo = this.actPoMapper.insert(po);
        return newPo.getObjectId();
    }

    @Override
    public void save(Activity bo) throws RuntimeException{
        ShareActPo po = cloneObj(bo, ShareActPo.class);
        this.actPoMapper.save(po);
    }
}
