

# Uninstall old versions
sudo apt-get remove docker docker-engine docker.io containerd runc

# Install using the repository

## Set up the repository
sudo apt-get -y update

sudo apt-get -y install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# # Las tres lineas (No funciona!) de arriba sustituye con esta 
# curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine 

sudo apt-get -y update
sudo apt-get -y install docker-ce docker-ce-cli containerd.io 
# Solucion  problema de " runtime.v1alpha2.RuntimeService"
sudo rm /etc/containerd/config.toml
sudo systemctl restart containerd





