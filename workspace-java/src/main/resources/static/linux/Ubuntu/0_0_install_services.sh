sudo apt-get -y update
sudo apt-get -y install firewalld
sudo apt-get install socat -y
sudo apt-get install conntrack -y

sudo sysctl -w net.ipv4.ip_forward=1

sudo firewall-cmd --add-port=443/TCP --permanent
sudo firewall-cmd --reload

