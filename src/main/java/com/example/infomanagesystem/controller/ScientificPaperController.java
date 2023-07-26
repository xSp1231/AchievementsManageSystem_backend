package com.example.infomanagesystem.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.infomanagesystem.entity.ScientificPaper;
import com.example.infomanagesystem.entity.Student;
import com.example.infomanagesystem.result.R;
import com.example.infomanagesystem.service.ScientificPaperService;
import com.example.infomanagesystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author xushupeng
 * @Date 2023-07-24 15:01
 */
@RestController
@CrossOrigin
@RequestMapping("/ScientificPaper")
public class ScientificPaperController {
    @Autowired
    private ScientificPaperService scientificPaperService;

    @GetMapping("/getAll")
    public R getAll(){
        List<ScientificPaper> scientificPaperList=scientificPaperService.getAllScientificPaper();
        return new R(true ,200,"所有科技论文",scientificPaperList);
    }
    //分页+条件查询
    @GetMapping("/{currentPage}/{pageSize}") //http://localhost:8080/ScientificPaper/2/3
    public R getPage(@PathVariable int currentPage,@PathVariable int pageSize, ScientificPaper scientificPaper){
        System.out.println("分页查询中的student is"+scientificPaper);
        return new R(true,200,"分页信息",scientificPaperService.getPage(currentPage,pageSize,scientificPaper));
    }

    @GetMapping("/getUserInfo")   //学生修改个人信息的时候 先要获取该学生的信息 前端携带token
    public R getUserInfo(HttpServletRequest request){
        // 获取 Authorization 头部的值
        String token = request.getHeader("Authorization").substring(7);
        if(JwtUtils.validateToken(token)){
            System.out.println("jwt正确");
            String username=JwtUtils.getUsernameFromToken(token);
            String role=JwtUtils.getRoleFromToken(token);
            System.out.println(" jwt  username "+username);
            System.out.println(" jwt  role "+role);
            return new R(true,200,"获得登录者信息",scientificPaperService.getUserScientificPaper(username));
        }
        else{
            System.out.println("jwt过期或错误");
            return new R(false,400,"jwt过期或错误");
        }
    }
    //获取编辑时的成果信息
    @GetMapping("getScientificPaperById/{id}")
    public R getScientificPaperById(@PathVariable Integer id){
        ScientificPaper temp=scientificPaperService.getScientificPaperById(id);
        if(temp!=null){ //找到了
            return new R(true,200,"找到了该成果id对应的信息",temp);
        }
        else{
            return new R(false,404,"not found");
        }
    }


    //管理员 学生 添加信息
    @PostMapping("/add")
    public R addScientificPaper(@RequestBody ScientificPaper scientificPaper) {
        if (scientificPaperService.saveScientificPaper(scientificPaper)) { //前端传过来的数据必须全面
            return new R(true, 201, "科技信息填报上传成功,待审核");
        }
        else{
            return new R(false, 201, "科技信息填报上传失败!,请检查是否有重复填报。如有,可以选择编辑或删除操作");
        }
    }
    //根据id删除单个
    @PostMapping("/deleteOne/{id}")
    public R deleteOne(@PathVariable Integer id){
      if(scientificPaperService.deleteScientificPaper(id)){
          return new R(true,200,"删除科技论文成果信息成果成功!");
      }
      else{
          return new R(false,400,"删除科技论文成果信息成果失败!");
      }
    }
    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        System.out.println("前端传过来的ids is "+ids);
        scientificPaperService.deleteBatch(ids);
        System.out.println("批量删除成功");
        return new R(true,204,"批量删除成功");
    }
    //修改科技论文的信息
    @PostMapping("/update")
    public R updateStudent(@RequestBody ScientificPaper scientificPaper){
        //用户名 角色 不能修改
        if(scientificPaperService.updateScientificPaper(scientificPaper)){
            return new R(true,200,"修改科技成果信息成功,等待审核");
        }
        else{
            return new R(false,403,"修改科技成果信息失败");
        }
    }

    //导入excel文件
    @PostMapping("/importData")
    public R importData(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用EasyExcel读取Excel文件 //输入流读取数据
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), ScientificPaper.class, new ScientificPaperListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return new R(true,200,"数据导入成功(用户名和title同时重复的数据将自动排除)");
    }
    //监听器
    private class ScientificPaperListener extends AnalysisEventListener<ScientificPaper> {
        @Override
        public void invoke(ScientificPaper scientificPaper, AnalysisContext analysisContext) {
            // 读取到一条数据时的回调 //子类继承父类并重写父类中的方法时，返回类型必须是父类方法返回类型的子类型或相同类型
            System.out.println("读取一行"+scientificPaper);
            boolean add=scientificPaperService.saveScientificPaper(scientificPaper); //插入数据库  如果存在 就不添加
        }
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            // 读取完成时的回调
            System.out.println("科技论文的excel文件读取完成");
        }
    }

    //导出全部所有的科技论文信息
    @GetMapping("/exportAll")
    public void exportData(HttpServletResponse response) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=ScientificPaperInfo.xlsx");
        // 查询数据库中的数据
        List<ScientificPaper> scientificPaperList = scientificPaperService.getAllScientificPaper(); //获取所有学生
        //创建 ExcelWriter 对象  输出流 // 输出流 向文件中写入
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out, ScientificPaper.class).build();

        // 创建 Sheet 对象
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();

        // 写入数据
        excelWriter.write(scientificPaperList, writeSheet);
        // 关闭 ExcelWriter 对象
        excelWriter.finish();
    }

    //根据id 导出所选的数据
    @PostMapping("/exportByIds")
    public void exportAll(@RequestBody List<Integer> ids ,HttpServletResponse response) throws IOException { //apifox 直接json传递一个数组 ["username1","username2","username3"]
        List<ScientificPaper> scientificPaperList =scientificPaperService.getByids(ids);
        System.out.println("接受到的列表数据"+scientificPaperList);
        // 导出数据到 Excel 文件并写入响应体
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=ScientificPaperData.xlsx");
        OutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream, ScientificPaper.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();
        excelWriter.write(scientificPaperList, writeSheet);
        excelWriter.finish();
        outputStream.flush();
    }

}
