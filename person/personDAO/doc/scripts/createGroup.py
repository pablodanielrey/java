import ldap
import ldap.modlist as modlist
import uuid

def remove_value(list, value):
	if value in list:
		list.remove(value)

try:
	l2 = ldap.initialize("ldap://127.0.0.1:389")
	l2.protocol_version = ldap.VERSION3
	l2.simple_bind_s("cn=admin,dc=econo","clave")

	gId = -1

        result = l.search_s("dc=econo",ldap.SCOPE_SUBTREE,"(objectClass=posixGroup)",["gidNumber"])
	if result == None:
		print "No se puede obtener los gids"
		exit()

	for (dn,attrs) in result:
		gidAux = int(attrs['gidNumber'][0])
		if gId < gidAux:
			gId = gidAux

	print gId + 1

	group = {}
# 	tipos = x-dcsys-office x-dcsys-alias x-dcsys-position x-dcsys-ou x-dcsys-timetable x-dcsys-profile
#	group['objectClass'] = ['top','posixGroup','x-dcsys-group','x-dcsys-entidad']
	group['objectClass'] = ['top','posixGroup','x-dcsys-group','x-dcsys-entidad','x-dcsys-profile']

	group['cn'] = 'admin-assistance'
	group['gidNumber'] = str(gId)
	

	group['memberUid'] = ['34296005']


	suuid = str(uuid.uuid4())
	group['x-dcsys-uuid'] = suuid

	dn = 'x-dcsys-uuid=' + suuid + ',ou=groups,dc=econo'
	l2.add_s(dn,modlist.addModlist(group))

	l2.unbind_s()

except ldap.LDAPError, e:
	print e

