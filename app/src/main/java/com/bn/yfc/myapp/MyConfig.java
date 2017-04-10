package com.bn.yfc.myapp;

/**
 * Created by yfc on 2016/12/18.
 */

public class MyConfig {
    //static String PPath = "http://192.168.1.115";
    static String PPaths = "";
    static String PPath = "http://120.77.87.193/index.php";
    //登录
    //user:手机号
    //password:密码,md5加密之后上传
    //type:类型(1:客户端登录,2:司机端登录)
    //code=1:成功 --->data=用户信息
    //code=101:账户被冻结
    //code=105:账号或密码错误
    //code=102:账户已登录
    //code=106:用户未完善个人资料 ---->同时登录成功
    //code=107:该用户未注册
    public static String POSTLOGINURL = PPath + "/client/user/login";
    //注册
    //user:手机号
    //password:密码,md5加密之后上传
    //type:类型(1:客户端注册,2:司机端注册)
    //phonetype:操作系统(A:安卓,I:IOS)
    //code=1:成功
    //code=103:用户名已存在
    //code=110:数据库写入失败
    public static String POSTREGISTERURL = PPath + "/client/user/register";
    //审核
    //level:申请类型,1:快递员,2:跑客,3:物流公司
    //cartype:1:摩托,2:轿车,3:小面包,4:中面包,5:小货车,6:中货车,7:大货车,8:物流公司,9:船,10:顺路送
    //idcard:身份证正面图
    //idback:身份证背面图
    //drivinglicense:驾照图
    //carlicense:行驶证图
    //bankcar:银行卡图
    //businesslicense:营业执照图
    //transportlicense:道路运输许可证图
    //code=1:成功
    //code=111:图片上传失败
    //code=110:个人信息保存失败
    public static String POSTEXAMINEURL = PPath + "/client/user/examine";
    //完善个人信息
    //id:用户id
    //type:类型(1:客户端登录,2:司机端登录)
    //sex:性别
    //realname:客户端为昵称,司机端为真实姓名
    //phone:手机号
    //pic:头像
    //age:年龄
    //code=1:成功
    //code=111:图片上传失败
    //code=109:个人信息保存失败
    public static String POSTOERSNALURL = PPath + "/client/user/personal";
    //获取审核信息
    //id:用户id
    //code=1:成功 ----> data =信息
    //code=110:获取信息失败
    public static String POSTGETEXAMINEURL = PPath + "/client/user/getexamine";
    //注销
    //id:用户id号
    //type:类型(1:客户端注册,2:司机端注册)
    //code=1:成功
    //code=106:退出失败
    public static String POSTLOGOUTURL = PPath + "/client/user/logout";
    //上下班
    //id:用户id号
    //do:操作,1:上班,2:下班
    //code=1:成功
    //code=110:失败
    public static String POSTWORKRESTURL = PPath + "/client/user/workrest";
    //余额
    //id:用户id
    //type:类型(1:客户端登录,2:司机端登录)
    //code=1:成功
    //code=108:查询余额失败
    public static String POSTGETMONEYURL = PPath + "/client/user/getmoney";
    //短信验证码
    //user:手机号
    //type:1=客户端,2=司机端
    //code:1=成功 ------->shotmes=验证码;data=时间戳
    //code:110 = 失败;
    //code:103=用户已存在;
    public static String POSTSHOTMESURL = PPath + "/client/user/shotmes";
    //找回密码
    //user:手机号
    //type:1=客户端,2=司机端
    //password:新密码
    //code:1 =成功
    //code:110=失败
    public static String POSTFINDBANKURL = PPath + "/client/user/findback";
    //添加银行卡
    //uid:用户id号
    //type:类型,1:客户端用户,2:司机端用户
    //bank:银行名称
    //user:用户真实姓名
    //card:卡号
    //pos:分行名
    //1:成功
    //110:失败
    public static String POSETADDBANKURL = PPath + "/client/bank/addbank";
    //获取银行卡
    //uid:用户id号
    //type:类型,1:客户端用户,2:司机端用户
    //1:成功
    //110:失败
    public static String POSTSHOWBANKURL = PPath + "/client/bank/showbank";
    //删除银行卡
    //id :银行卡id
    public static String POSTDELETEBANKURL = PPath + "/client/bank/deletebank";
    //客服电话
    //无传参
    public static String POSTGETPHONENUMURL = PPath + "/client/user/getphonenum";
    //提交意见
    //uid:用户id
    //content:反馈内容
    //type:1用户,2司机
    public static String POSTSENGBACKURL = PPath + "/client/user/sendback";
    //修改登录账号
    //type:1客户端,2司机端
    //id:用户id
    //user:新用户名
    public static String POSTCHANGEUSRE = PPath + "/client/user/changeuser";
    //微信快捷登录
    public static String POSTWXICONURL = PPath + "/client/user/sendwxpic";
    //发布(快递/顺路送/船运)订单
    public static String POSTORDERADDURL = PPath + "/client/order/orderadd";
    //请求省代理数据
    public static String POSTSHENGURL = PPath + "/client/become/getsheng";
    //请求市代理数据
    public static String POSTSHIURL = PPath + "/client/become/getshi";
    //请求区代理数据
    public static String POSTQUURL = PPath + "/client/become/getqu";
    //申请成为代理
    //uid == 用户ID
    //cid==地理地址id
    //money== 代理地址价格
    public static String POSTBECOMEURL = PPath + "/client/become/become";
    //请求常见问题
    //type:司机端2|客户端1
    public static String POSTGETQANDAURL = PPath + "/client/user/getqanda";
    //微信登录
    //
    public static String POSTWXLOGINURL = PPath + "/client/user/wxlogin";
    //微信注册
    public static String POSTWXADDURL = PPath + "/client/user/wxadd";
    //微信资料上传
    public static String POSTWXDATAURL = PPath + "/client/user/sendwxpic";
    //获取个人订单信息
    //uid:用户id
    //num订单条数(数值为10的倍数，每次请求加10条)
    //status:1:已完成,2:未完成,3:待协商,4:待评价,不传参表示请求全部订单
    public static String POSTORDERURL = PPath + "/client/order/userorder";
    //获取订单详情
    //id = 订单id
    public static String POSTSHOWORDERS = PPath + "/client/order/showorder";
    //司机到达请求
    //id:订单id
    public static String POSTDRIVERSENDURL = PPath + "/client/order/driversend";
    //用户确认送达
    public static String POSTUSERSENDURL = PPath + "/client/order/usersend";
    //开始派送
    public static String POGOSENDURL = PPath + "/client/order/gosend";
    //取消订单
    //id:订单id
    public static String POSTDELETEORDERURL = PPath + "/client/order/deleteorder";
    //协商订单
    //id:订单id
    public static String POSTBEXSURL = PPath + "/client/order/bexs";
    //获取已接订单
    //num:需要条数(默认10,表示第1-10条.20表示1-20.100表示1到100)
    //status:1:已完成,2:未完成,3:待协商,4:待评价,不传是为查询所有订单
    public static String POSTDRIVAERORDERURL = PPath + "/client/order/driverorder";
    //评论订单
    //id=订单id
    //
    public static String POSTCOMMENTURL = PPath + "/client/order/comment";
    //保险费用
    //data:保险费比例
    public static String POSTPROTECTURL = PPath + "/client/money/protect";
    //路费计算规则
    public static String POSTSENDURL = PPath + "/client/money/send";
    //获取合作商家链接
    public static String POSTGETFIRESNDURL = PPath + "/client/friend/getfriend";
    //运费规则
    //三个参数合并
    public static String POSTRULESURL = PPath + "/client/rules/getrules";
    //消息中心接口
    public static String POSTURLMESGURL = PPath + "/client/message/message";
    //邀请人列表
    public static String POSTMYSONSURL = PPath + "/client/user/mysons";
    //帮我买支付完成
    //id= 订单id
    //money= 支付金额
    public static String POSTBWORDERURL = PPath + "/client/order/bwm_order";
    public static String POSTGETNUM = PPath + "/client/user/get_num";
    public static String POSTGETMONEYSURL = PPath + "/client/money/getmoney";
    public static String POSTINTEGRALRECORDURL = PPath + "/client/integral/integral_record";
    public static String POSTHEATRECORDURL = PPath + "/client/integral/heart_record";
    public static String POSTBRABANCHURL = PPath + "/client/integral/branch";
    public static String POSTEXCTITATIONURL = PPath + "/client/integral/recommend";
    public static String POSTSTOREURL = PPath + "/client/friend/getshangjia";
    public static String POSTQUSURL = PPath + "/client/friend/search_qu";
    public static String POSTGETSTOREDATAURL = PPath + "/client/friend/merchant_detail";
    public static String POSTSEASTOREDATAURL = PPath + "/client/friend/search";
    public static String POSTSEACHSTORENAMEURL = PPath + "/client/friend/search_name";
    public static String POSTCHONGZHIRUL = PPath + "/client/money/goto_cz";
    public static String POSTWXPAYURL = "http://120.77.87.193/index.php/client/Wxapp/app_pay";
    public static String POSTALIPAYURL = PPath + "/client/recorded/pay";
    public static String POSTSENDTIXURL = "http://120.77.87.193/php_df/check.php";
    public static String POSTSENDMONEYURL = "http://120.77.87.193/php_df/sortpay.php";
    public static String POSTSHOWSTOREURL = "http://120.77.87.193/Client/User/sortchange";
}
