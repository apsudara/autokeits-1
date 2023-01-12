# source: https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/
# Considerations about apiserver-advertise-address and ControlPlaneEndpoint

# kubeadm init  --apiserver-advertise-address=$MASTER --pod-network-cidr $CALICOIP/16