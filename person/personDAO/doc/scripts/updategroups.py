import ldap
import ldap.modlist as modlist
import ldap.filter as lfilter
import uuid

def remove_value(list, value):
	if value in list:
		list.remove(value)

try:
	l = ldap.initialize("ldap://127.0.0.1:389")
	l.protocol_version = ldap.VERSION3
	l.simple_bind_s("cn=admin,dc=econo","clave");

	result = l.search_s("ou=groups,dc=econo",ldap.SCOPE_SUBTREE,"(&(objectClass=x-dcsys-group)(memberUid=*))",["dn","cn","memberUid","objectClass"])
	l.unbind_s()

	if result == None:
		print "Ningun resultado"
		exit()

	l2 = ldap.initialize("ldap://127.0.0.1:388")
	l2.protocol_version = ldap.VERSION3
	l2.simple_bind_s("cn=admin,dc=econo","clave")

	for r in result:
	
		if "x-dcsys-profile" in r[1]['objectClass']:
			continue


		attrs = r[1]
		cn = attrs['cn']		

		if cn[0] in ["presi-admin-assistance","presi-user-assistance","admin-users","unlp","perfiles","admin-assistance","PRESIDENCIA"]:
			continue;

		if "admin" == cn[0]:
			continue;

		if "PRESIDENCIA" == cn[0]:
			continue;

		if "perfiles" == cn[0]:
			continue;




#		print cn[0]
#		continue


#		print "buscando " + cn[0]
		filter = "(&(objectClass=x-dcsys-group)(cn=" + lfilter.escape_filter_chars(cn[0]) +"))"
		
#		print "filter " + filter
		result2 = l2.search_s("ou=groups,dc=econo",ldap.SCOPE_SUBTREE,filter,["dn","cn","memberUid"])
		r2 = result2[0]
		dn = r2[0]
		originalMemberUids = r2[1]['memberUid']
		memberUids = r[1]['memberUid']

		print cn[0]
		for m in memberUids:
			if m not in originalMemberUids:
				originalMemberUids.append(m)
				print "agregue : " + m


		mods = [(ldap.MOD_REPLACE,'memberUid',originalMemberUids)]

#		print "--------------------"
#		print dn
#		print cn[0]
#		print mods
#		print "----------------------"

		l2.modify_s(dn,mods)

		

#		if "gosaMailAlternateAddress" in attrs:
#			mail = attrs["gosaMailAlternateAddress"]
#			print "transformando el mail alternativo de gosa a mail dcsys " + mail 
#			del attrs["gosaMailAlternateAddress"]
#			attrs["x-dcsys-mail"] = mail

#		dn = "x-dcsys-uuid=" + str(uuid.uuid4()) + ",ou=groups,dc=econo"
#		atts = modlist.addModlist(attrs)
#		print "creando :" + dn
#		print attrs
			
#		l2.add_s(dn,atts)
	
	l2.unbind_s()

except ldap.LDAPError, e:
	print e

