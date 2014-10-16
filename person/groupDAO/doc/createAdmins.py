import ldap
import ldap.modlist as modlist
import uuid

try:
        l = ldap.initialize("ldap://127.0.0.1:389")
        l.protocol_version = ldap.VERSION3
        l.simple_bind_s("cn=admin,dc=econo","clave");
        

	objectClass = ['top','x-dcsys-entidad','x-dcsys-group','posixGroup']
	memberUid = ['27294557','31381082']

	xuuid = str(uuid.uuid4())
	l.add_s("x-dcsys-uuid=" + xuuid + ",ou=groups,dc=econo",[('objectClass',objectClass),('memberUid',memberUid),('x-dcsys-uuid',xuuid),('gidNumber','0'),('cn','admin')]);

        l.unbind_s()

except ldap.LDAPError, e:
        print e

