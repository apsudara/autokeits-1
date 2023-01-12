echo "[$(date +%h:%m:%s/%DD-%MM-%YY)] [EXECUTE] firewall-cmd --zone='public' --permanent --add-port=10250/tcp"
sudo firewall-cmd --zone='public' --permanent --add-port=10250/tcp
echo "[$(date +%h:%m:%s/%DD-%MM-%YY)] [EXECUTE] firewall-cmd --zone='public' --permanent --add-port=30000-32767/tcp"
sudo firewall-cmd --zone='public' --permanent --add-port=30000-32767/tcp

sudo firewall-cmd --reload


