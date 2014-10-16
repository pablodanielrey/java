#!/bin/bash
echo -e 'dn: cn=config\nreplace: olcLogLevel\nolcLogLevel: acl stats'|ldapmodify
