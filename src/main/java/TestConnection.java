
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;

public class TestConnection {

    public static void main(String[] args) {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            System.out.println("✅ Connected successfully!");
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
