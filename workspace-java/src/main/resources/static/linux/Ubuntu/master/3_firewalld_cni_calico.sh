sudo firewall-cmd --add-rich-rule="rule family=ipv4 source address=192.168.0.0/16 accept" --permanent

sudo firewall-cmd --reload