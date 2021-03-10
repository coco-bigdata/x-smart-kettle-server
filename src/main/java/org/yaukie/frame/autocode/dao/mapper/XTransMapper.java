package org.yaukie.frame.autocode.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.yaukie.frame.autocode.model.XTrans;
import org.yaukie.frame.autocode.model.XTransExample;

public interface XTransMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    long countByExample(XTransExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int deleteByExample(XTransExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int insert(XTrans record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int insertSelective(XTrans record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    List<XTrans> selectByExample(XTransExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    XTrans selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int updateByExampleSelective(@Param("record") XTrans record, @Param("example") XTransExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int updateByExample(@Param("record") XTrans record, @Param("example") XTransExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int updateByPrimaryKeySelective(XTrans record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_trans
     *
     * @mbg.generated Tue Feb 02 11:07:55 CST 2021
     */
    int updateByPrimaryKey(XTrans record);
}