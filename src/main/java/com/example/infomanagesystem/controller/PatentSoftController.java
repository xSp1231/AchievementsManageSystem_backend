package com.example.infomanagesystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;

import com.example.infomanagesystem.entity.Message;
import com.example.infomanagesystem.entity.PatentSoft;
import com.example.infomanagesystem.result.R;

import com.example.infomanagesystem.service.MessageService;
import com.example.infomanagesystem.service.PatentSoftService;
import com.example.infomanagesystem.utils.JwtUtils;
import com.example.infomanagesystem.utils.MyFunction;
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
 * @Date 2023-07-25 18:12
 */
@RestController
@CrossOrigin
@RequestMapping("/PatentSoft")
public class PatentSoftController {
    @Autowired
    private PatentSoftService patentSoftService;
    @Autowired
    private MessageService messageService;
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/getAll")
    public R getAll(){
        List<PatentSoft> patentSoftList=patentSoftService.getAllPatentSoft();
        return new R(true ,200,"所有专利软著",patentSoftList);
    }

    @SaCheckLogin //分页+条件查询
    @GetMapping("/{currentPage}/{pageSize}") //http://localhost:8080/PatentSoft/2/3
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, PatentSoft patentSoft){
        System.out.println("分页查询中的patentSoft is"+patentSoft);
        return new R(true,200,"分页信息",patentSoftService.getPage(currentPage,pageSize,patentSoft));
    }

    @GetMapping("/getUserInfo")   // 获取该学生的科技论文表  前端携带token
    public R getUserScientificPaper() {
        System.out.println("个人信息会话号码(账号)"+ StpUtil.getLoginId());
        String username= (String) StpUtil.getLoginId();
        //之后根据username 获取相应的列表
        return new R(true,200,"获得登录者的成果信息",patentSoftService.getUserPatentSoft(username));
    }
    //获取编辑时的成果信息
    @SaCheckLogin
    @GetMapping("getPatentSoftById/{id}")
    public R getPatentSoftById(@PathVariable Integer id){
        PatentSoft temp=patentSoftService.getPatentSoftById(id);
        if(temp!=null){ //找到了
            return new R(true,200,"找到了该成果id对应的信息",temp);
        }
        else{
            return new R(false,404,"not found");
        }
    }


    //管理员 学生 添加信息
    @SaCheckLogin
    @PostMapping("/add")
    public R addPatentSoft(@RequestBody PatentSoft patentSoft) {
        if (patentSoftService.savePatentSoft(patentSoft)) { //前端传过来的数据必须全面
            return new R(true, 201, "专利/软著信息填报上传成功,待审核");
        }
        else{
            return new R(false, 201, "专利软著填报上传失败!,请检查是否有重复填报 或者用户名是否存在");
        }
    }
    //根据id删除单个
    @SaCheckLogin
    @PostMapping("/deleteOne/{id}")
    public R deleteOne(@PathVariable Integer id){
        if(patentSoftService.deletePatentSoft(id)){
            return new R(true,200,"删除专利/软著成果信息成果成功!");
        }
        else{
            return new R(false,400,"删除专利/软著成果信息成果失败!");
        }
    }
    @SaCheckLogin
    @PostMapping("/deleteBatch")
    public R deleteBatch(@RequestBody List<Integer> ids){
        System.out.println("前端传过来的ids is "+ids);
        patentSoftService.deleteBatch(ids);
        System.out.println("批量删除成功");
        return new R(true,204,"批量删除成功");
    }
    //修改专著的信息
    @SaCheckLogin
    @PostMapping("/update")
    public R updatePatentSoft(@RequestBody PatentSoft patentSoft){
        //管理员审核更新成果的时候
        if(patentSoft.getStatus().equals("拒绝")){
            Message message = new Message();
            message.setMessage(patentSoft.getRefuseInfo());//设置拒绝消息
            message.setUsername(patentSoft.getUsername());
            message.setStatus(patentSoft.getStatus()); //成果状态 拒绝 接收 。。
            message.setProject("专利软著表");//成果属于那张表
            message.setName(patentSoft.getName()); //获取成果名
            message.setIsreading("0");//未读
            message.setAudittime(MyFunction.getCurrentDateTime());
            messageService.save(message);
        }
        if(patentSoft.getStatus().equals("接收")){
            Message message = new Message();
            message.setMessage("成果审核成功");//设置拒绝消息
            message.setUsername(patentSoft.getUsername());
            message.setStatus(patentSoft.getStatus()); //成果状态 拒绝 接收 。。
            message.setProject("专利软著表");//成果属于那张表
            message.setName(patentSoft.getName()); //获取成果名
            message.setIsreading("0");//未读
            message.setAudittime(MyFunction.getCurrentDateTime());
            messageService.save(message);
        }
        //用户名 角色 不能修改
        if(patentSoftService.updatePatentSoft(patentSoft)){
            return new R(true,200,"修改专利/软著成果信息成功,等待审核");
        }
        else{
            return new R(false,403,"修改专著成果信息失败");
        }
    }

    //导入excel文件
    @PostMapping("/importData")
    public R importData(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用EasyExcel读取Excel文件 //输入流读取数据
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(), PatentSoft.class, new PatentSoftListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
        return new R(true,200,"数据导入成功(用户名和name同时重复的数据将自动排除)");
    }
    //监听器
    private class PatentSoftListener extends AnalysisEventListener<PatentSoft> {
        @Override
        public void invoke(PatentSoft patentSoft, AnalysisContext analysisContext) {
            // 读取到一条数据时的回调 //子类继承父类并重写父类中的方法时，返回类型必须是父类方法返回类型的子类型或相同类型
            System.out.println("读取一行"+patentSoft);
            boolean add=patentSoftService.savePatentSoft(patentSoft); //插入数据库  如果存在 就不添加
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
        response.setHeader("Content-Disposition", "attachment;filename=PatentSoftInfo.xlsx");
        // 查询数据库中的数据
        List<PatentSoft> patentSoftList = patentSoftService.getAllPatentSoft(); //获取所有学生
        //创建 ExcelWriter 对象  输出流 // 输出流 向文件中写入
        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out, PatentSoft.class).build();

        // 创建 Sheet 对象
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();

        // 写入数据
        excelWriter.write(patentSoftList, writeSheet);
        // 关闭 ExcelWriter 对象
        excelWriter.finish();
    }

    //根据id 导出所选的数据
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/exportByIds")
    public void exportAll(@RequestBody List<Integer> ids ,HttpServletResponse response) throws IOException { //apifox 直接json传递一个数组 ["username1","username2","username3"]
        List<PatentSoft> patentSoftList =patentSoftService.getByids(ids);
        System.out.println("接受到的列表数据"+patentSoftList);
        // 导出数据到 Excel 文件并写入响应体
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=PatentSoftInfo.xlsx");
        OutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream, PatentSoft.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet0").build();
        excelWriter.write(patentSoftList, writeSheet);
        excelWriter.finish();
        outputStream.flush();
    }
}
