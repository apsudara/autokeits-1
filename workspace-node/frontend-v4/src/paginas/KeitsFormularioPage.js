import React, { useState, useEffect } from 'react'
import * as Formulario from 'reactstrap'
import '../estilos/paginas/KeitsFormularioPage.scss'
import '../estilos/componentes/Button.scss'
import { useNavigate,useLocation } from "react-router-dom"
// import {Button as Buton} from '../componentes/Button'

function KeitsFormularioPage() {
    
    const location = useLocation();
    let projectname = location.state === null || location.state === undefined ? { projectname: "default" } : location.state;

    const [campo1, setCampo1] = useState([]);

    const [campo2, setCampo2] = useState([]);

    const [credentialsAll, setCredentialsAll] = useState([{}]);

    const [credentialsMasters, setCredentialsMasters] = useState(["default"]);
    const handleCredSelectMaster = (indice, res) => {

        // console.log("Lista SELECTED s: indice=" + indice + "  lista"+ credentialsMasters)
        let aux_cre = credentialsMasters;
        aux_cre[indice] = res.target.value;
        setCredentialsMasters([...aux_cre]);

        let aux_campo1 = campo1;
        aux_campo1[indice]['credname'] = res.target.value;
        setCampo1([...aux_campo1]);
        // console.log("Lista SELECTED e: indice=" + indice + "  lista"+ credentialsMasters)
    }

    const [credentialsWrokers, setCredentialsWorkers] = useState(["default"]);
    const handleCredSelectWorkers = (indice, res) => {

        // console.log("Lista SELECTED s: indice=" + indice + "  lista"+ credentialsWrokers)
        let aux_cre = credentialsWrokers;
        aux_cre[indice] = res.target.value;
        setCredentialsWorkers([...aux_cre]);

        let aux_campo2 = campo2;
        aux_campo2[indice]['credname'] = res.target.value;
        setCampo2([...aux_campo2]);
        // console.log("Lista SELECTED e: indice=" + indice + "  lista"+ credentialsWrokers)
    }

    useEffect(
        () => {
            const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/credentials";
            const getCredentials2 = async () => {
                try {
                    const response = await fetch(urlEx1);
                    const data = await response.json();
                    console.log(data);
                    setCredentialsAll(data);
                } catch (err) {
                    setCredentialsAll([{}]);
                }
            }
            getCredentials2();
        }
        , []);

    const handleRefreshAddButtonMaster = (events) => {
        events.preventDefault();
        let nuevoCampo = { host: "", creds: { user: "", password: "" }, credname: "" };
        setCampo1([...campo1, nuevoCampo]);

        let scrd2 = ["default"]
        console.log("Lista SELECTED: " + credentialsMasters)
        setCredentialsMasters([...credentialsMasters, scrd2]);
    }

    const handleHostMaster = (indice, event) => {
        let d = [...campo1];
        d[indice][event.target.name] = event.target.value;
        if (indice === 0) {
            d[indice].isCPEndpoint = true;
        }
        setCampo1(d);



    }


    const handleRefreshAddButtonWorker = (events) => {
        events.preventDefault();
        let nuevoCampo = { host: "", creds: { user: "root", password: "vagrant" } };
        setCampo2([...campo2, nuevoCampo]);
        console.log(campo2)
    }

    const handleHostWorker = (indice, event) => {
        let d = [...campo2];
        d[indice][event.target.name] = event.target.value;
        console.log("event.target.name:" + event.target.name + " event.target.value:" + event.target.value)
        setCampo2(d);
    }

    const handleRefreshQuitarButtonMaster = (param1) => {
        let a = [...campo1];
        a.splice(param1, 1)
        setCampo1(a)
    }
    const handleRefreshQuitarButtonWorker = (param1) => {
        let a = [...campo2];
        a.splice(param1, 1)
        setCampo2(a)
    }

    let navigate = useNavigate();
    const handleDeployButton = (formDataEx) => {
        setConfiguracion(formDataEx);
        
        console.log( "[DEBUG] project state value:   ");
        console.log(projectname);
        
        console.log(location.state);
        if(projectname.projectname === null || projectname.projectname === undefined){
            formDataEx.projectname="default";
        }

        const putCongs = async () => {
            const head = {
                method: "put",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formDataEx)
            }
            const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + formDataEx.name;
            await fetch(urlEx1, head).then(data => {
                setResExist({ data });
            }, (error) => {
                console.log(error);
            });

            // console.log(data);
        };
        putCongs();

        navigate('/consoleLogs/',{state: {...formDataEx} });

    }

    const [name, setName] = useState("");
    const [resExist, setResExist] = useState(false);
    const handleNameUpdate = (res) => {
        const isNameExists = async () => {
            if (res.target.value !== "") {
                const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + res.target.value + "/exist";
                const response = await fetch(urlEx1,);
                const data = await response.json();
                // console.log(data);
                setResExist(data.isError);
            }
        };
        isNameExists();
        setName(res.target.value);
    };
    const [ssoo, setSsoo] = useState("");
    const handleSsooUpdate = (res) => {
        setSsoo(res.target.value);
    };

    const [cni, setCNI] = useState("");
    const handleCniUpdate = (res) => {
        setCNI(res.target.value);
    };

    const [cri, setCRI] = useState("");
    const handleCriUpdate = (res) => {
        setCRI(res.target.value);
    };

    const [configuracion, setConfiguracion] = useState({});


    useEffect(() => {
        const putConfig = async () => {
            console.log(configuracion)
        }
        putConfig();
    }, [configuracion]);

    const formDataEx = {
        "name": name,
        "ssoo": ssoo,
        "cni": cni,
        "cri": cri,
        "dashboard": false,
        "projectname": projectname.projectname,
        "masterHosts": [...campo1],
        "workerHosts": [...campo2]
    };
    return (
        <div className='keitsFormularioPage'>
            <Formulario.Form >
                <Formulario.FormGroup >
                    <Formulario.Label for="name">
                        Name
                    </Formulario.Label>
                    <Formulario.Input type="text" value={name} onChange={handleNameUpdate} />
                    <Formulario.FormFeedback className={resExist ? 'invalid-feedback' : 'invalid-feedback-hide'}>
                        Kubernetes '{name}'  already exist !
                    </Formulario.FormFeedback>
                    <Formulario.FormGroup className='cni-div'>
                        <Formulario.Label for="ssoo">
                            Operating System
                        </Formulario.Label>
                        <select value={ssoo}
                            onChange={handleSsooUpdate}>
                            <option value="CentOS7">CentOS 7/8</option>
                            <option value="Ubuntu">Ubuntu</option>
                            {/* <option value="windows">Windows Server 2019</option> */}
                        </select>
                    </Formulario.FormGroup>
                    <Formulario.FormGroup className='cni-div'>
                        <Formulario.Label for="cni">
                            Container Network Interface
                        </Formulario.Label>
                        <select value={cni}
                            onChange={handleCniUpdate}>
                            <option value="ACI">ACI (Cisco Application Centric Infrastructure)</option>
                            <option value="Antrea">Antrea</option>
                            <option value="aws-cni">AWS VPC CNI for Kubernetes</option>
                            <option value="Calico">Calico</option>
                            <option value="Cilium">Cilium</option>
                            <option value="CNI-Genie">CNI-Genie from Huawei</option>
                            <option value="cni-ipvlan-vpc-k8s">cni-ipvlan-vpc-k8s</option>
                            <option value="Coil">Coil</option>
                            <option value="Contiv-VPP">Contiv-VPP</option>
                            <option value="Contrail">Contrail / Tungsten Fabric</option>
                            <option value="Flannel">Flannel</option>
                            <option value="Hybridnet">Hybridnet</option>
                            <option value="Jaguar">Jaguar</option>
                            <option value="k-vswitch">k-vswitch</option>
                            <option value="Knitter">Knitter</option>
                            <option value="Kube-OVN">Kube-OVN</option>
                            <option value="Kube-router">Kube-router</option>
                            <option value="L2-networks">L2 networks and linux bridging</option>
                            <option value="Multus">Multus (a Multi Network plugin)</option>
                            <option value="OVN4NFV-K8s-Plugin">OVN4NFV-K8s-Plugin (OVN based CNI controller & plugin)</option>
                            <option value="NSX-T">NSX-T</option>
                            <option value="OVN">OVN (Open Virtual Networking)</option>
                            <option value="Weave">Weave Net from Weaveworks</option>
                        </select>
                    </Formulario.FormGroup>
                    <Formulario.FormGroup className='cni-div'>
                        <Formulario.Label for="cri" >
                            Container Runtime Interface
                        </Formulario.Label>
                        <select value={cri}
                            onChange={handleCriUpdate}>
                            <option value="cri-o">CRI-O</option>
                            <option value="containerd">Docker Engine</option>
                            <option value="marantis">Mirantis Container Runtime</option>
                        </select>
                    </Formulario.FormGroup>                    
                    <Formulario.FormGroup className='etcd'>
                        <Formulario.Label for="etcd" >
                            etcd
                        </Formulario.Label>
                        <select value="Stacked">
                            <option value="cri-o">Stacked</option>
                            <option value="containerd">External</option>
                        </select>
                    </Formulario.FormGroup>
                    <Formulario.FormGroup className='master'>
                        <Formulario.Label for="node-master">
                            Node/s Master
                        </Formulario.Label>

                        {campo1.map((entrada, indice) => {
                            return (
                                <div key={indice} className="node-cols">
                                    <Formulario.Input placeholder='Hostname / IP' name='host' value={entrada.host} onChange={event => handleHostMaster(indice, event)} />
                                    {/* <Formulario.Input placeholder='Credential' name='creds' className='form-control-creds' value={entrada.creds.user} onChange={event => handleHostMaster(indice, event)} /> */}


                                    <select value={credentialsMasters[indice] + ""}
                                        onChange={evento => handleCredSelectMaster(indice, evento)}>
                                        <option value="select" >select</option>)
                                        {

                                            credentialsAll.map((credname1, indice1) => {
                                                return (
                                                    <option value={credname1.name}>{credname1.name}</option>)
                                            })
                                        }
                                    </select>
                                    <Formulario.Button onClick={event => handleRefreshQuitarButtonMaster(indice, event)}  >remove</Formulario.Button>
                                </div>
                            )
                        })}
                        <Formulario.Button className='btn input-btn' onClick={handleRefreshAddButtonMaster}>add</Formulario.Button>
                    </Formulario.FormGroup>
                    <Formulario.FormGroup className='worker'>
                        <Formulario.Label for="node-worker">
                            Node/s Worker
                        </Formulario.Label >
                        {campo2.map((entrada, indice) => {
                            return (
                                <div key={indice} className="node-cols">
                                    <Formulario.Input placeholder='Hostname / IP' name='host' value={entrada.host} onChange={event => handleHostWorker(indice, event)} />
                                    <select value={credentialsWrokers[indice] + ""}
                                        onChange={evento => handleCredSelectWorkers(indice, evento)}>
                                        <option value="select" >select</option>)
                                        {

                                            credentialsAll.map((credname1, indice1) => {
                                                return (
                                                    <option value={credname1.name}>{credname1.name}</option>)
                                            })
                                        }
                                    </select>
                                    <Formulario.Button onClick={event => handleRefreshQuitarButtonWorker(indice, event)}>remove</Formulario.Button>
                                </div>
                            )
                        })}
                        <Formulario.Button className='btn input-btn' onClick={handleRefreshAddButtonWorker} >add</Formulario.Button>

                    </Formulario.FormGroup>

                    <Formulario.Button className='btn input-btn input-btn-apply' value="Apply" onClick={event => handleDeployButton(formDataEx, event)}>Deploy</Formulario.Button>
                </Formulario.FormGroup>
            </Formulario.Form>
        </div>
    )
}


export default KeitsFormularioPage