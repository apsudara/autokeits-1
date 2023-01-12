import { React, useEffect, useState } from 'react';
import { Card, CardBody, CardHeader, Table, Button, Row } from 'reactstrap';
import { NavLink, useNavigate } from "react-router-dom"
import '../estilos/paginas/ProjectPage.scss'
import * as iconsri from "react-icons/ri"
import * as iconsmd from "react-icons/md"

function KubernetesPage() {
  const navigate = useNavigate();
  const [configs, setConfig] = useState([{}]);
  const [resExist, setResExist] = useState(false);
  const [getall,setall] = useState(false);
 
  useEffect(
    () => {
      if(configs.length === 1) {
        console.log(configs.length)
        setall(true);
      }
      const getConfigs = async () => {
        if( getall){
          try {
            let base_uri = process.env.REACT_APP_BACKEND_BASE_URI
            const response = await fetch(base_uri + '/api/v1/kubernetes?page=all');
            const data = await response.json();
            console.log(data);
            setConfig(data);
            setall(false);
          } catch (err) {
            setConfig([{}]);
          }
        }
      };
      getConfigs();
    });


  const [triggerCreateConfig, setTriggerCreateConfig] = useState(false);
  const triggerCreate = () => {
    setTriggerCreateConfig(true);
  }
  const [changelogoOP, setChangeLogoOP] = useState("");
  const triggerChageLogoOP = (form) => {
    setChangeLogoOP("Selected")
    // navigate("/kubernetes/formulario/on-premise")
    navigate("/kubernetes/formulario")
  }
  const [changelogoEKS, setChangeLogoEKS] = useState("");
  const triggerChageLogoEKS = (form) => {
    setChangeLogoEKS("Selected")
    navigate("/kubernetes/formulario/eks")
  }
  const [changelogoAKS, setChangeLogoAKS] = useState("");
  const triggerChageLogoAKS = (form) => {
    setChangeLogoAKS("Selected")
    navigate("/kubernetes/formulario/aks")
  }
  const [changelogoGKE, setChangeLogoGKE] = useState("");
  const triggerChageLogoGKE = (form) => {
    setChangeLogoGKE("Selected")
    navigate("/kubernetes/formulario/eks")
  }
  const handleDelete = (res) => {
    if (res !== "") {
      const deleteElement = async (res) => {
        const head = {
          method: "delete",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({})
        }
        const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + res;
        try {
          const response = await fetch(urlEx1, head);
          const data = await response.json();
          setResExist(data.isError);
        } catch (error) {
          setResExist(true);
        }
      }
      deleteElement(res);
      setall(true);
    }
  }
  return (
    <div className='projectPage' >
      <Card className='card-projects card-projects-left'>
        <CardHeader>
          Configurations
        </CardHeader>
        <CardBody>
          <Table>
            <thead>
              <tr>
                <th>
                  Project
                </th>
                <th>
                  Name
                </th>
                <th className='btn-project'>
                  <Button id="create" onClick={
                    // () => {}
                    triggerCreate
                  }>+ create</Button>
                </th>
              </tr>
            </thead>
            <tbody>
              {
                configs.map((val, indice) =>
                  <tr key={indice} >
                    <td>
                      {val.projectname}
                    </td>
                    <td>
                      {val.configname}
                    </td>
                    <td className="settings">
                      <iconsri.RiEdit2Line style={{ color: 'green' }} />
                      <iconsmd.MdDeleteOutline style={{ color: 'darkred' }} onClick={() => { handleDelete(val.configname) }} />
                    </td>
                  </tr>
                )
              }
            </tbody>
          </Table>
        </CardBody>
      </Card>
      {
        triggerCreateConfig ? (
          <Card className='card-projects card-projects-right card-kubernetes-rigth' >
            <CardHeader>
              <div style={{ float: 'left', width: '50%', textAlign: 'left', fontWeight: 'bold', color: 'gray' }}>
                New Cluster Kubernetes
              </div>
              <div>
                <Button onClick={() => { setTriggerCreateConfig(false) }}>X</Button>
              </div>
            </CardHeader>
            <CardBody className='card-body-sel-provider'>
              <Table>
                <thead>
                  <tr>
                    <th>
                      On-Promise
                    </th>
                    <th>
                      Providers Cloud
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td className='td-hide '>
                      <div className="select-type-provider td-kubernetes" onClick={() => triggerChageLogoOP()}>
                        Virtual Machines {changelogoOP}
                      </div>
                    </td>
                    <td className='td-hide'>
                      <div className="select-type-provider- td-kubernetes" onClick={() => triggerChageLogoAKS()}>
                        AKS {changelogoAKS}
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td className='td-hide'></td>
                    <td className='td-hide'>
                      <div className="select-type-provider td-kubernetes" onClick={() => triggerChageLogoEKS()}>
                        EKS {changelogoEKS}
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td className='td-hide'></td>
                    <td className='td-hide'>
                      <div className="select-type-provider td-kubernetes" onClick={() => triggerChageLogoGKE()}>
                        GKE {changelogoGKE}
                      </div>
                    </td>
                  </tr>
                </tbody>
              </Table>
            </CardBody>
          </Card>
        ) : console.log()
      }
    </div>
  );
}


export default KubernetesPage
