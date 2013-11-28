import ldap
import ldap.modlist as modlist
import ldap.filter as lfilter
import uuid
import psycopg2

def remove_value(list, value):
	if value in list:
		list.remove(value)

try:

	l2 = ldap.initialize("ldap://127.0.0.1:389")
	l2.protocol_version = ldap.VERSION3
	l2.simple_bind_s("cn=admin,dc=econo","clave")


	sqls = ['select g.name, p.dni from groups g, persons p, groups_persons gp where g.id = gp.groups_id and p.id = gp.persons_id order by g.name',
		'select g.name, p.dni from positions g, persons p, positions_persons gp where g.id = gp.position_id and p.id = gp.persons_id order by g.name',
		'select g.name, p.dni from organizational_units g, persons p, organizational_units_persons gp where g.id = gp.organizationalunit_id and p.id = gp.persons_id order by g.name']

	con = psycopg2.connect(host='127.0.0.1', port='5433', database='reloj3', user='reloj2', password='clave')
	cur = con.cursor()

	for sql in sqls:
		cur.execute(sql)
	
		data = 1
		while data != None:
			data = cur.fetchone()
			if data != None:
				name = data[0]
				dni = data[1]

				if name in ["presi-admin-assistance","presi-user-assistance","admin-users","unlp","perfiles","admin-assistance","PRESIDENCIA"]:
        		                continue;

	        	        filter = "(&(objectClass=x-dcsys-group)(cn=" + lfilter.escape_filter_chars(name) +"))"

		                result2 = l2.search_s("ou=groups,dc=econo",ldap.SCOPE_SUBTREE,filter,["dn","cn","memberUid"])
        		        r2 = result2[0]
	        	        dn = r2[0]
        	        	originalMemberUids = r2[1]['memberUid']

        	       	        if dni not in originalMemberUids:
					originalMemberUids.append(dni)
                	                print "agregue : " + dni + " al grupo " + name
		        	        mods = [(ldap.MOD_REPLACE,'memberUid',originalMemberUids)]
	        			l2.modify_s(dn,mods)



	con.close()
	l2.unbind_s()

except ldap.LDAPError, e:
	print e

