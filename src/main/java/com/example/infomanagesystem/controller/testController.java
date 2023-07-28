//package com.example.infomanagesystem.controller;
//
//import cn.dev33.satoken.annotation.*;
//import cn.dev33.satoken.stp.StpUtil;
//import cn.dev33.satoken.util.SaResult;
//import com.example.infomanagesystem.result.R;
//import com.example.infomanagesystem.utils.UploadUtil;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * @Author xushupeng
// * @Date 2023-07-20 13:59
// */
//
////关于一些功能的测试使用
//@RestController
//@CrossOrigin
//public class testController {
//    //文件上传接口 上传到本地
//    @PostMapping("/upload")
//    public R uploadFile(@RequestParam(value = "file",required = false) MultipartFile file){
//        if(file.isEmpty()){
//            return new R(false,400,"文件为空");
//        }
//        String OriginalFilename=file.getOriginalFilename();//获取原始文件名
//        //System.out.println("原始文件名是"+OriginalFilename); //xxx.png  xxx.jpg
//        //使用时间戳 防止文件重名 构造文件名 hello.png
//        String fileName=System.currentTimeMillis()+"."+OriginalFilename.substring(OriginalFilename.lastIndexOf(".")+1); //获取最后一个字符为”.“的下标再+1 之后字符串截取
//        //另外一种命名方式  String fileName=OriginalFilename.substring(0,OriginalFilename.lastIndexOf('.'))+ UUID.randomUUID().toString()+OriginalFilename.substring(OriginalFilename.lastIndexOf('.'));
//        System.out.println("得到的filename is "+fileName);//类似于
//        String path="C:\\Users\\86187\\Desktop\\testimgs\\"; //设置图片的保存位置
//        File dest=new File(path+fileName);//根据路径创建一个新的文件对象
//        if(!dest.getParentFile().exists()){ //判断父目录是否存在  即path 是否存在
//            dest.mkdirs();
//        }
//        try {
//              file.transferTo(dest);////file.transferTo(dest)是Java中java.io.File类的一个方法，它的作用是将当前文件对象表示的文件内容传输到指定的目标位置。
//              return new R(true,200,"图片上床成功",path+fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//              return new R(false,400,"图片上传失败");
//        }
//    }
//
//    //文件上传到阿里云
//    @PostMapping("/uploadToOssTest")
//    public R uploadToOss(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException{
//        return new R(true,200, UploadUtil.uploadImage(file),"存储到阿里云Oss");
//    }
//
//    //satoken 测试
//
//    // 测试登录，浏览器访问： http://localhost:8080/user/doLogin?username=zhang&password=123456
//
//    @RequestMapping("/user/doLogin")
//    public String doLogin(String username, String password) {
//        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
//        if("zhang".equals(username) && "123456".equals(password)) {
//            StpUtil.login("xsp");
//
//            return "登录成功"+"token is "+StpUtil.getTokenValue() +" token name is "+StpUtil.getTokenName()+"token有效期 "+StpUtil.getTokenTimeout();
//        }
//        return "登录失败";
//    }
//
//    // 查询登录状态，浏览器访问： http://localhost:8080/user/isLogin
//    @RequestMapping("/user/isLogin")
//    public String isLogin() {
//        System.out.println("该账号所拥有的权限有"+StpUtil.getPermissionList());
//        System.out.println(StpUtil.getRoleList());
////        System.out.println(StpUtil.hasPermission("user.add"));
////        System.out.println(StpUtil.hasPermission("goods.add"));
//        return "当前会话是否登录：" + StpUtil.isLogin();
//    }
//
//    @SaCheckLogin   //// 登录校验：只有登录之后才能进入该方法
//    @RequestMapping("/checkLogin")
//    public String checkLogin(String username, String password) {
//        return " 登录之后才可以访问";
//    }
//
//
//    @SaCheckRole("admin")   //// 只有具有admin身份才可以使用  //指定角色
//    @RequestMapping("/checkRole")
//    public String checkRole(String username, String password) {
//        return " 拥有角色才可以访问";
//    }
//
//    @SaCheckPermission("user.add")  //拥有指定权限才能进入访问   访问者必须要拥有指定权限才可以访问
//    @RequestMapping("add")
//    public String add() {
//        return "拥有指定权限才能访问";
//    }
//    @RequestMapping("test")  // SaMode.OR  只要登录者具有其中的一个权限就可以 访问   SaMode.AND  具有全部权限
//    @SaCheckPermission(value = "user.test" ,orRole = {"admin,user"})  //orRole 字段代表权限校验未通过时的次要选择，两者只要其一校验成功即可进入请求方法
//    public SaResult atJurOr() {
//        return SaResult.data("用户信息");
//    }
//
//    @RequestMapping("ignore")  // SaMode.OR  只要登录者具有其中的一个权限就可以 访问   SaMode.AND  具有全部权限
//    @SaIgnore
//    public String ignore() {
//        return "该接口忽略权限";
//    }
//
//    // 在 `@SaCheckOr` 中可以指定多个注解，只要当前会话满足其中一个注解即可通过验证，进入方法。
//    @SaCheckOr(
//            role = @SaCheckRole("user"),
//            permission = @SaCheckPermission("user.test")
//    )  //都不可满足  访问不了
//    @RequestMapping("ortest")
//    public String test() {
//        // ...
//        return "ortest";
//    }
//
//
//
//
//    @RequestMapping("/user/logout")
//    public SaResult logout() {
//        StpUtil.logout();
//        return SaResult.ok();
//    }
//
//
//}
