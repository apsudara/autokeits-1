
# Hacer solamente la primera vez."
# echo "[$(date +%h:%m:%s/%DD-%MM-%YY)] kubeadm init  --apiserver-advertise-address=$MASTER --pod-network-cidr $DOCKERIP/16"
echo "> INIT CLUSTER MASTER NODE"
echo "> mkdir -p /root/.kube"
mkdir -p /root/.kube

echo "> cp /etc/kubernetes/admin.conf /root/.kube/config"
cp /etc/kubernetes/admin.conf /root/.kube/config
echo "> chown $(id -u):$(id -g) /root/.kube/config"
chown $(id -u):$(id -g) /root/.kube/config

# Crear una vez"

echo "> wget https://docs.projectcalico.org/manifests/tigera-operator.yaml --no-check-certificate"
wget https://docs.projectcalico.org/manifests/tigera-operator.yaml --no-check-certificate
echo "> kubectl create -f tigera-operator.yaml"
kubectl create -f tigera-operator.yaml

# Optional"
echo "> wget https://docs.projectcalico.org/manifests/custom-resources.yaml --no-check-certificate"
wget https://docs.projectcalico.org/manifests/custom-resources.yaml --no-check-certificate

# TODO: change ip in custom-resources.yaml"
echo "> kubectl apply -f custom-resources.yaml"
kubectl apply -f custom-resources.yaml


