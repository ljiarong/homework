package cn.edu.xmu.oomall.freight.mapper.generator;

import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
@Mapper
public interface ShopLogisticsPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @Delete({
        "delete from freight_shop_logistics",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @Insert({
        "insert into freight_shop_logistics (`shop_id`, `logistics_id`, ",
        "`secret`, `creator_id`, ",
        "`creator_name`, `modifier_id`, ",
        "`modifier_name`, `gmt_create`, ",
        "`gmt_modified`, `invalid`, ",
        "`priority`)",
        "values (#{shopId,jdbcType=BIGINT}, #{logisticsId,jdbcType=BIGINT}, ",
        "#{secret,jdbcType=VARCHAR}, #{creatorId,jdbcType=BIGINT}, ",
        "#{creatorName,jdbcType=VARCHAR}, #{modifierId,jdbcType=BIGINT}, ",
        "#{modifierName,jdbcType=VARCHAR}, #{gmtCreate,jdbcType=TIMESTAMP}, ",
        "#{gmtModified,jdbcType=TIMESTAMP}, #{invalid,jdbcType=TINYINT}, ",
        "#{priority,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(ShopLogisticsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @InsertProvider(type=ShopLogisticsPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(ShopLogisticsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @SelectProvider(type=ShopLogisticsPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="logistics_id", property="logisticsId", jdbcType=JdbcType.BIGINT),
        @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="invalid", property="invalid", jdbcType=JdbcType.TINYINT),
        @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER)
    })
    List<ShopLogisticsPo> selectByExample(ShopLogisticsPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @Select({
        "select",
        "`id`, `shop_id`, `logistics_id`, `secret`, `creator_id`, `creator_name`, `modifier_id`, ",
        "`modifier_name`, `gmt_create`, `gmt_modified`, `invalid`, `priority`",
        "from freight_shop_logistics",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="logistics_id", property="logisticsId", jdbcType=JdbcType.BIGINT),
        @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="invalid", property="invalid", jdbcType=JdbcType.TINYINT),
        @Result(column="priority", property="priority", jdbcType=JdbcType.INTEGER)
    })
    ShopLogisticsPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @UpdateProvider(type=ShopLogisticsPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") ShopLogisticsPo record, @Param("example") ShopLogisticsPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @UpdateProvider(type=ShopLogisticsPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") ShopLogisticsPo record, @Param("example") ShopLogisticsPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @UpdateProvider(type=ShopLogisticsPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ShopLogisticsPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbggenerated
     */
    @Update({
        "update freight_shop_logistics",
        "set `shop_id` = #{shopId,jdbcType=BIGINT},",
          "`logistics_id` = #{logisticsId,jdbcType=BIGINT},",
          "`secret` = #{secret,jdbcType=VARCHAR},",
          "`creator_id` = #{creatorId,jdbcType=BIGINT},",
          "`creator_name` = #{creatorName,jdbcType=VARCHAR},",
          "`modifier_id` = #{modifierId,jdbcType=BIGINT},",
          "`modifier_name` = #{modifierName,jdbcType=VARCHAR},",
          "`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP},",
          "`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP},",
          "`invalid` = #{invalid,jdbcType=TINYINT},",
          "`priority` = #{priority,jdbcType=INTEGER}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ShopLogisticsPo record);
}