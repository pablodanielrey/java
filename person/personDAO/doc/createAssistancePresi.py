import ldap
import ldap.modlist as modlist
import uuid

try:
        l = ldap.initialize("ldap://127.0.0.1:389")
        l.protocol_version = ldap.VERSION3
        l.simple_bind_s("cn=admin,dc=econo","clave");
        

	objectClass = ['top','x-dcsys-entidad','x-dcsys-group','posixGroup']

	#de administracion general
	#lalli mariel patricia dni: 16.875.681
	#leones sergio adrian  dni: 28.327.027
	# pablo, emanue, mariel, sergio
	memberUid = ['27294557','31381082','16875681','28327027']

	xuuid = str(uuid.uuid4())
	cn = 'presi-admin-assistance'
	l.add_s("x-dcsys-uuid=" + xuuid + ",ou=groups,dc=econo",[('objectClass',objectClass),('memberUid',memberUid),('x-dcsys-uuid',xuuid),('gidNumber','1000'),('cn',cn)]);


	#usuarios de administracion de personal
	#carlos alberto freire dni: 12.291.469
	#stella maris salazar dni: 13.621.733
	#albertini rossotti  dni: 27.677.198

	memberUid = ['12291469','13621733','27677198']
	xuuid = str(uuid.uuid4())
	cn = 'presi-user-assistance'
	l.add_s("x-dcsys-uuid=" + xuuid + ",ou=groups,dc=econo",[('objectClass',objectClass),('memberUid',memberUid),('x-dcsys-uuid',xuuid),('gidNumber','1001'),('cn',cn)]);

        l.unbind_s()

except ldap.LDAPError, e:
        print e

