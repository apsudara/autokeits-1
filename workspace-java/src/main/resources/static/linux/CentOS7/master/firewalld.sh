# # access from workers
# # NFS
# NFS='192.168.188.46'
# firewall-cd --add-rich-rule="rule family=ipv4 source address=$NFS/32 accept" --permanent
# MASTER='192.168.188.47'
# firewall-cd --add-rich-rule="rule family=ipv4 source address=$MASTER/32 accept" --permanent
# WORKER1="192.168.188.48"
# firewall-cd --add-rich-rule="rule family=ipv4 source address=$WORKER1/32 accept" --permanent
# WORKER2='192.168.188.52'
# firewall-cd --add-rich-rule="rule family=ipv4 source address=$WORKER2/32 accept" --permanent
# firewall-cmd --reload

# # Fuente: https://docs.docker.com/network/network-tutorial-standalone/
# # access container to loalhost (Docker IP por defecto)
# DOCKERIP='172.17.0.0'
# firewall-cd --add-rich-rule="rule family=ipv4 source address=$DOCKERIP/16 accept"
# firewall-cmd --reload