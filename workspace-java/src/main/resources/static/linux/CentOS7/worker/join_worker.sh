# Get hash
MASTER_HASH=$(openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //')
MASTER_TOKEN=$(kubeadm token list | awk '{print $1}' | tail -n 1)


kubeadm join --discovery-token $MASTER_TOKEN --discovery-token-ca-cert-hash sha256:$MASTER_HASH
