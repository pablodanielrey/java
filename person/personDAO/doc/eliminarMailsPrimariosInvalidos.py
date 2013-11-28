import ldap
import ldap.modlist as modlist
import uuid

def remove_value(list, value):
	if value in list:
		list.remove(value)

try:
	l = ldap.initialize("ldap://127.0.0.1:389")
	l.protocol_version = ldap.VERSION3
	l.simple_bind_s("cn=admin,dc=econo","clave");

	users_to_sync = l.search_s("ou=people,dc=econo",ldap.SCOPE_SUBTREE,"(&(objectClass=person)(mail=*))",["mail"])
	if users_to_sync == None:
		exit()

	l.unbind_s();	

	l2 = ldap.initialize("ldap://127.0.0.1:389")
	l2.protocol_version = ldap.VERSION3
	l2.simple_bind_s("cn=admin,dc=econo","clave")

	for u in users_to_sync:
		dn = u[0]
		attrs = u[1]
		
		if "mail" not in attrs:
			continue
		mail = attrs["mail"][0]

		try:	
			domain = mail[mail.index('@') + 1 : ]
			if (domain.startswith('econo') | domain.startswith('depeco')):
				continue

			print "modificando " + dn + " " + mail

		except ValueError, e:
			print "eliminando : " + dn + " " +  mail
		
		mods = [(ldap.MOD_DELETE,'mail', None)]

		try:
			l2.modify_s(dn,mods)
		except ldap.LDAPError, e:
		        print e


	
	l2.unbind_s()

except ldap.LDAPError, e:
	print e

