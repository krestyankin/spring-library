package ru.krestyankin.library.security;

import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FailsafeJdbcMutableAclService extends JdbcMutableAclService {
    private final LookupStrategy lookupStrategy;

    public FailsafeJdbcMutableAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
        this.lookupStrategy = lookupStrategy;
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) {
        Map<ObjectIdentity, Acl> result = lookupStrategy.readAclsById(objects, sids);
        return result;
    }

    @Override
    public Acl readAclById(ObjectIdentity object, List<Sid> sids) {
        Map<ObjectIdentity, Acl> map = readAclsById(Arrays.asList(object), sids);
        if (map.containsKey(object))
          return map.get(object);
        log.warn("Unable to find ACL information for object identity "+object);
        return createAcl(object);
    }

    @Override
    public MutableAcl createAcl(ObjectIdentity objectIdentity) {
        try {
            return super.createAcl(objectIdentity);
        }
        catch (AlreadyExistsException alreadyExists) {
            return (MutableAcl) readAclById(objectIdentity);
        }
    }
}
