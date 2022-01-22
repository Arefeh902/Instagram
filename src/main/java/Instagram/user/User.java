package Instagram.user;

import Instagram.main.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity
public class User {

    public static User currentUser;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String phoneNum;
    private String email;

    public String getUsername() {
        return username;
    }

    private User() {
    }

    private User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User create(){
        return new User();
    }

    public static User create(String username, String password){
        return new User(username, password);
    }

    public static Boolean createAndAddToDataBase(String username, String password){
        User user = new User(username, password);
        System.out.println(username);
        System.out.println(isUsernameUnique(username));
        if(isUsernameUnique(username)){
            return addToDatabase(user);
        }
        else{
            return Boolean.FALSE;
        }
    }

    public static Boolean isUsernameUnique(String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.where(builder.equal(root.get("username"), username));
        List<User> users = session.createQuery(criteria).getResultList();
        if(users.size() != 0){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static Boolean addToDatabase(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);

            transaction.commit();
        }

        catch (Exception e) {
            if (transaction!=null) transaction.rollback();
            e.printStackTrace();
            return Boolean.FALSE;
        } finally {
            session.close();
        }
        return Boolean.TRUE;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}