#!/bin/bash
echo -e "dn: olcDatabase={2}hdb,cn=config\nreplace: olcDbConfig\nolcDbConfig: {0}set_cachesize 0 83886080 0\nolcDbConfig: {1}set_lk_max_objects 50000\nolcDbConfig: {2}set_lk_max_locks 50000\nolcDbConfig: {3}set_lk_max_lockers 50000" | ldapmodify
