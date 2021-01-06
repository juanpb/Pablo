package p.pruebas;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * User: P_BENEDETTI
 * Date: 9/3/2020
 * Time: 12:18
 */
public class LDAP {

    public static void main(String[] args) throws Exception {
        pruebaUsuariofinal();
        //String x = getUid("Prueba1oxlog");
        //String x = getUid("SeroxlogC");
        //System.out.println("x = " + x);
        //System.out.println("getMembers() = " + getMembers(""));
    }

    private static void prueba() {
        // Set up the environment for creating the initial context
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.167.52.100:389/UID=Prueba1oxlog,ou=usuarios,ou=tmoviles,o=telefonica");
        //env.put(Context.PROVIDER_URL, "ldap://10.167.52.100:389/UID=prueoxir,ou=usuarios,ou=tmoviles,o=telefonica");

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "UID=prueoxir,ou=usuarios,ou=tmoviles,o=telefonica");
        env.put(Context.SECURITY_CREDENTIALS, "Telco2018*");
        //env.put(Context.SECURITY_CREDENTIALS, "8kxTbSGPZ$");

// Create the initial context
        try {
            DirContext x = new InitialDirContext(env);
            /*SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> search = x.search("ou=usuarios,ou=tmoviles,O=TELEFONICA", "uid=prueoxir", sc);

            while (search.hasMore()) {
                System.out.println(search.next());
            }  */
            System.out.println("Terminó bien");

        } catch (NamingException e) {
            e.printStackTrace();
        }
// ... do something useful with ctx
    }
    private static void pruebaUsuariofinal() {
        Hashtable env = new Hashtable();
        String password = "*****";
        password = "Telco2020*";
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.167.52.100:389/UID=Prueba1oxlog,ou=usuarios,ou=tmoviles,o=telefonica");

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "UID=Prueba1oxlog,ou=usuarios,ou=tmoviles,o=telefonica");
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            DirContext x = new InitialDirContext(env);
            System.out.println("Autenticación correcta");

        } catch (NamingException e) {
            System.out.println("Autenticación incorrecta");
        }
// ... do something useful with ctx
    }

    private static String getUid(String user) throws Exception {
        DirContext ctx = ldapContext();
        String filter = "(uid=" + user + ")";
        SearchControls ctrl = new SearchControls();
        ctrl.setSearchScope(SearchControls.OBJECT_SCOPE);
        NamingEnumeration answer = ctx.search("", filter, ctrl);
        String dn;
        if (answer.hasMore()) {
            SearchResult result = (SearchResult) answer.next();
            dn = result.getNameInNamespace();
        } else {
            dn = null;
        }

        answer.close();
        return dn;

    }

    private static String getMembers(String user) throws Exception {
        String uc = "UID=Prueba3oxlog,ou=usuarios,ou=tasa,o=telefonica";
        DirContext ctx = ldapContext();
        //String filter = "UID=Prueba3oxlog,ou=usuarios,ou=tasa,o=telefonica";//"(objectclass=grupos)";
        String filter = "ou=grupos,o=telefonica";//"(objectclass=grupos)";
        SearchControls ctrl = new SearchControls();
        String[] attr = {"cn, value"};
        ctrl.setReturningAttributes(attr);
        ctrl.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        NamingEnumeration answer = ctx.search(uc, filter, ctrl);
        String dn;
        if (answer.hasMore()) {
            SearchResult result = (SearchResult) answer.next();
            dn = result.getNameInNamespace();
        } else {
            dn = null;
        }

        answer.close();
        return dn;

    }

    private static DirContext ldapContext() throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        //env.put(Context.PROVIDER_URL, "ldap://10.167.52.100:389/UID=Prueba1oxlog,ou=usuarios,ou=tmoviles,o=telefonica");
        env.put(Context.PROVIDER_URL, "ldap://10.167.52.100:389/UID=SeroxlogC,ou=servicios,o=telefonica");

// Authenticate as S. User and password "mysecret"
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        // env.put(Context.SECURITY_AUTHENTICATION, "none");
        //env.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
        //env.put(Context.SECURITY_PRINCIPAL, "ou=usuarios,ou=tmoviles,o=telefonica");
        env.put(Context.SECURITY_PRINCIPAL, "UID=SeroxlogC,ou=servicios,o=telefonica");
        env.put(Context.SECURITY_CREDENTIALS, "Telco2020*");
        //env.put(Context.SECURITY_CREDENTIALS, "8kxTbSGPZ$");
        return  new InitialDirContext(env);

    }

}
