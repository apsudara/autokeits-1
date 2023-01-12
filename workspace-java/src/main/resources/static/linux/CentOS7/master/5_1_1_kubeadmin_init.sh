 
kubeadm init --node-name "master.kubernetes.int"   --control-plane-endpoint="master_this.endpointControlPanel"
--apiserver-advertise-address="master"
--pod-network-cidr "this.ipCNI/16"
--upload-certs --certificate-key "certificate_key"

