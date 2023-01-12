
sed -i -e "s|mirrorlist=|#mirrorlist=|g" /etc/yum.repos.d/CentOS-*
sed -i -e "s|#baseurl=http://mirror.centos.org|baseurl=http://mirror.centos.org|g" /etc/yum.repos.d/CentOS-*
dnf --disablerepo '*' --enablerepo=extras swap centos-linux-repos centos-stream-repos -y
dnf distro-sync -y

sudo systemctl start firewalld


sudo swapoff -a
sudo sh -c '''echo "net.bridge.bridge-nf-call-ip6tables = 1" > /etc/sysctl.d/k8s.conf'''
sudo sh -c '''echo "net.bridge.bridge-nf-call-iptables = 1" >> /etc/sysctl.d/k8s.conf'''
sudo sysctl --system

sudo yum install conntrack socat wget -y

# para los certificados
sudo yum install ntpdate -y
sudo ntpdate pool.ntp.org

# para la comprobación de conexiones entre nodos (api server control plane)
sudo yum install nc -y 