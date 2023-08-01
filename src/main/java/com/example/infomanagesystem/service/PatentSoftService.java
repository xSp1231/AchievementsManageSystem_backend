package com.example.infomanagesystem.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.PatentSoft;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-25 17:46
 */
public interface PatentSoftService extends IService<PatentSoft>  {
    List<PatentSoft> getAllPatentSoft();

    //得到登录者的专利软著成果信息
    List<PatentSoft> getUserPatentSoft(String username);

    //编辑的时候 根据成果的id来获取相应成果的填报信息  //用户确定更改 状态为审核
    PatentSoft getPatentSoftById(Integer id);

    boolean savePatentSoft(PatentSoft patentSoft); //专著信息的增加    管理员 学生

    boolean deletePatentSoft(Integer id);//专著信息的删除 注销 (管理员 学生)
    //批量删除
    void deleteBatch(List<Integer> ids); //批量删除 通过传入的一组ids 来删除
    //修改
    boolean updatePatentSoft(PatentSoft patentSoft); //专著信息的修改 (管理员 学生)
    //分页
    IPage<PatentSoft> getPage(int currentPage, int pageSize, PatentSoft patentSoft); //当前所在页 每页多少条(管理员)

    // 批量导出
    List<PatentSoft> getByids(List<Integer> ids); //查询id在ids这个数组里面的所有数据
    Boolean deleteAllPatentSoftOfUsername(String username);
}
