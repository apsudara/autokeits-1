# sudo kubeadm token list |awk '{print $1}' | head -n 2 | tail -n 1 | tr '\n' ' '
kubeadm token list -o yaml | grep "auth" -B 3 | grep token | sed  's/token: //g' | tr '\n' ' '