package shiro.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * Created by Lembre on 2018.10.22
 */
public class ShiroRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //System.out.println("doGetAuthenticationInfo"+token);
        //1.把AuthenticationToken转换为UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //2.从UsernamePasswordToken获取username
        String username = upToken.getUsername();
        //3.调用数据库方法，从数据库中查询username对应的用户记录
        System.out.println("从数据库中获取的username:"+username+"所对应的用户信息");
        //4.用户不存在，可以抛出UnknownAccountException异常
        if("unknown".equals(username)){
            throw new UnknownAccountException("用户不存在");//这时候AuthenticationException就会捕获到用户不存在，使用
            //catch (AuthenticationException ae) {System.out.println("2.login fail:"+ae.getMessage());就可以看到用户不存在信息了
        }
        //5.根据用户情况，决定是否需要抛出其他的AuthenticationException异常对象
        if("monster".equals(username)){
            throw new LockedAccountException("用户被锁定");
        }
        //6.根据用户情况，来构建AuthorizationInfo对象，并返回,通常使用的实现类是SimpleAuthenticationInfo
        //以下信息是从数据库中获取的
        //1.principal：认证的实体信息，可以是username，也可以是数据库对应的用户的实体类对象
        Object principal = username;
        //2.credentials 密码
        Object credentials = null;/*"fc1709d0a95a6be30bc5926fdb7f22f4";*/
        if("admin".equals(username)){
            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
        }else if("user".equals(username)){
            credentials = "098d2c478e9c11555ce2823231e02ec1";
        }
        //3.realmName：当前的realm对象的name，待用弗雷德getName()即可。
        String realmName = getName();

        //4). 盐值.
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);

        SimpleAuthenticationInfo info = null;/*new SimpleAuthenticationInfo(principal,credentials,realmName);*/
        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return info;
        /* 因为上面是没有链接数据库的，所以，实际上只要用户名不是unknown和monster都表示已经从数据库中找到对应的用户信息，
        如果密码也匹配的话就能够跳转到页面，经过认证之后，所有的页面都可以访问了
        *  实际的数据库查询的例子:
        User user = null;
        try {
            user = loginService.selectByPrimaryKey(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断是否从数据库中查询到用户信息
        if (user == null)
        {
           throw new UnknownAccountException("用户不存在");
        }
        //从数据库查询到的密码
        String username_db = user.getUsername();

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getUsername(),this.getName());*/
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /*这个方法的目的是为了得到123456对应的MD5的值，以便比对*/
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("admin");
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }
}
