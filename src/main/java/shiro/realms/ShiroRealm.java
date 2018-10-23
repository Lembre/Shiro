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
        //1.��AuthenticationTokenת��ΪUsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //2.��UsernamePasswordToken��ȡusername
        String username = upToken.getUsername();
        //3.�������ݿⷽ���������ݿ��в�ѯusername��Ӧ���û���¼
        System.out.println("�����ݿ��л�ȡ��username:"+username+"����Ӧ���û���Ϣ");
        //4.�û������ڣ������׳�UnknownAccountException�쳣
        if("unknown".equals(username)){
            throw new UnknownAccountException("�û�������");//��ʱ��AuthenticationException�ͻᲶ���û������ڣ�ʹ��
            //catch (AuthenticationException ae) {System.out.println("2.login fail:"+ae.getMessage());�Ϳ��Կ����û���������Ϣ��
        }
        //5.�����û�����������Ƿ���Ҫ�׳�������AuthenticationException�쳣����
        if("monster".equals(username)){
            throw new LockedAccountException("�û�������");
        }
        //6.�����û������������AuthorizationInfo���󣬲�����,ͨ��ʹ�õ�ʵ������SimpleAuthenticationInfo
        //������Ϣ�Ǵ����ݿ��л�ȡ��
        //1.principal����֤��ʵ����Ϣ��������username��Ҳ���������ݿ��Ӧ���û���ʵ�������
        Object principal = username;
        //2.credentials ����
        Object credentials = null;/*"fc1709d0a95a6be30bc5926fdb7f22f4";*/
        if("admin".equals(username)){
            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
        }else if("user".equals(username)){
            credentials = "098d2c478e9c11555ce2823231e02ec1";
        }
        //3.realmName����ǰ��realm�����name�����ø��׵�getName()���ɡ�
        String realmName = getName();

        //4). ��ֵ.
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);

        SimpleAuthenticationInfo info = null;/*new SimpleAuthenticationInfo(principal,credentials,realmName);*/
        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return info;
        /* ��Ϊ������û���������ݿ�ģ����ԣ�ʵ����ֻҪ�û�������unknown��monster����ʾ�Ѿ������ݿ����ҵ���Ӧ���û���Ϣ��
        �������Ҳƥ��Ļ����ܹ���ת��ҳ�棬������֤֮�����е�ҳ�涼���Է�����
        *  ʵ�ʵ����ݿ��ѯ������:
        User user = null;
        try {
            user = loginService.selectByPrimaryKey(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //�ж��Ƿ�����ݿ��в�ѯ���û���Ϣ
        if (user == null)
        {
           throw new UnknownAccountException("�û�������");
        }
        //�����ݿ��ѯ��������
        String username_db = user.getUsername();

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getUsername(),this.getName());*/
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /*���������Ŀ����Ϊ�˵õ�123456��Ӧ��MD5��ֵ���Ա�ȶ�*/
    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("admin");
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }
}
