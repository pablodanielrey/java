#!/bin/bash
sudo -c 'ssh -L 4949:127.0.0.1:4848 -L 5433:127.0.0.1:5432 -L 3389:127.0.0.1:389 root@163.10.17.105'


