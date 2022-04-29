import com.cjf.service.Impl.UsertService;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@ComponentScan
public class UserTTest {
    @Resource
    private UsertService usertService;
    @Test
    public void test(){
        System.out.println(usertService);
    }
//    @Test
//    public void addUser(){
//        SqlSession sqlSession = myUtils.getSqlSession();
//        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
//        UserT u = new UserT();
//        u.setUsername("Johnson");
//        u.setPassword( "111111");
//        u.setEmail( "Johnson@whut.edu.cn");
//        u.setBirthday(new Date());
//        u.setMoney(200);
//        userDao.addUser(u);
//        sqlSession.commit();
//        sqlSession.close();
//    }
//    @Test
//    public void updateUser(){
//        SqlSession sqlSession = myUtils.getSqlSession();
//        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
//        UserT u = new UserT();
//        u.setUsername("Johnson");
//        u.setPassword( "2222212");
//        u.setEmail( "Johnson@whut.edu.cn");
//        u.setBirthday(new Date());
//        u.setMoney(400);
//        u.setId(8);
//        userDao.updateUser(u);
//        sqlSession.commit();
//        sqlSession.close();
//    }
//    @Test
//    public void deleteUser(){
//        SqlSession sqlSession = myUtils.getSqlSession();
//        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
//        userDao.deleteUser(8);
//        sqlSession.commit();
//        sqlSession.close();
//    }


}
