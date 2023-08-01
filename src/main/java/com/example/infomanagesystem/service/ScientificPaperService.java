package com.example.infomanagesystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.entity.Student;

import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-24 14:38
 */
public interface ScientificPaperService extends IService <ScientificPaper>{
    List<ScientificPaper> getAllScientificPaper();

    //得到登录者的科技论文成果信息
    List<ScientificPaper> getUserScientificPaper(String username);

    //编辑的时候 根据成果的id来获取相应成果的填报信息  //用户确定更改 状态为审核
    ScientificPaper getScientificPaperById(Integer id);

    boolean saveScientificPaper(ScientificPaper scientificPaper); //科技论文信息的增加    管理员 学生
    boolean deleteScientificPaper(Integer id);//科技论文的删除 注销 (管理员 学生)
    //批量删除
    void deleteBatch(List<Integer> ids); //批量删除 通过传入的一组ids 来删除
    //修改
    boolean updateScientificPaper(ScientificPaper scientificPaper); //科技论文的修改 (管理员 学生)
    //分页
    IPage<ScientificPaper> getPage(int currentPage, int pageSize, ScientificPaper scientificPaper); //当前所在页 每页多少条(管理员)

    // 批量导出
    List<ScientificPaper> getByids(List<Integer> ids); //查询id在ids这个数组里面的所有数据

    Boolean deleteAllScientificPaperOfUsername(String username);

}
