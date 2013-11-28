import ldap
import ldap.modlist as modlist
import uuid

try:
        l = ldap.initialize("ldap://127.0.0.1:389")
        l.protocol_version = ldap.VERSION3
        l.simple_bind_s("cn=admin,dc=econo","clave");
        

	l.add_s("dc=econo",[('objectClass',['top','dcObject','organization']),
			    ('o','fce'),
			    ('dc','econo')])

	objectClass = ['top','organizationalUnit']

	l.add_s("ou=groups,dc=econo",[('objectClass',objectClass),('ou','groups')]);
	l.add_s("ou=people,dc=econo",[('objectClass',objectClass),('ou','people')]);

        l.unbind_s()

except ldap.LDAPError, e:
        print e

