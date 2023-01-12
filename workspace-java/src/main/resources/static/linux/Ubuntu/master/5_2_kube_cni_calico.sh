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
