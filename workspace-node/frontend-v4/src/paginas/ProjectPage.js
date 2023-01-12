import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, CardHeader, Table, Label, Input, FormFeedback } from 'reactstrap';

import * as iconsri from "react-icons/ri"
import * as iconsmd from "react-icons/md"


export default function ProjectPage() {

  const [name, setName] = useState("");
  const [resExist, setResExist] = useState(false);
  const [getProjects, setProjects] = useState([{ name: "No Results!" }]);
  useEffect(() => {
    // console.log('Triggered setProjects! create ')
    const getProjs = async () => {
      try {
        const response = await fetch(process.env.REACT_APP_BACKEND_BASE_URI + '/api/v1/projects');
        const data = await response.json();
        setProjects(data);
      } catch (err) {
        setProjects([{}]);
      }
    };
    getProjs();
  },[resExist,getProjects]);
  const [ocultarCreateProj, setOculutarCreateProj] = useState(false);
  const triggerClickCreate = () => {
    console.log('Click create project Triggered!');
    setOculutarCreateProj(true);
  }
  const triggerClickAdd = () => {
    const putCongs = async () => {
      const head = {
        method: "put",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({})
      }
      const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/projects/" + name;
      try {
        const response = await fetch(urlEx1, head);
        const data = await response.json();
        setResExist(data.isError);
      } catch (error) {
        setResExist(false);
      }
    };
    if (!resExist) {
      putCongs();
      //navigate('/kubernetes');
    }

    //
  }
  const handleNameUpdate = (res) => {
    const isNameExists = async () => {
      if (res.target.value !== "") {
        const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/projects/" + res.target.value + "/exists";
        try {
          const response = await fetch(urlEx1);
          const data = await response.json();
          setResExist(data.isError);
        } catch (error) {
          setResExist(true);
        }

      }
    };
    isNameExists();
    setName(res.target.value);
  };

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
        const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/projects/" + res;
        try {
          const response = await fetch(urlEx1, head);
          const data = await response.json();
          setResExist(data.isError);
        } catch (error) {
          setResExist(true);
        }
      }
      deleteElement(res);
    }
  }
  let navigate = useNavigate();



  const [triggerCreateConfig, setTriggerCreateConfig] = useState(false);
  const [getProjectName,setProjectName]= useState("");
  const triggerCreate = (projectname) => {
    setProjectName(projectname)
    setTriggerCreateConfig(true);
  }
  const [changelogoOP, setChangeLogoOP] = useState("");
  const triggerChageLogoOP = (projectname) => {
    setChangeLogoOP("Selected")
    // navigate("/kubernetes/formulario/on-premise")
    let prname={ projectname: projectname}
    navigate("/kubernetes/formulario",{state: {...prname} })
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

  return (
    <div className='projectPage'>
      <Card className='card-projects card-projects-left'>
        <CardHeader>
          Projects
        </CardHeader>
        <CardBody>
          <Table>
            <thead>
              <tr>
                <th>
                  Name
                </th>
                <th className='btn-project'>
                  <Button id="create" onClick={
                    // () => {}
                    triggerClickCreate
                  }>+ create</Button>
                </th>
              </tr>
            </thead>
            <tbody>
              {
                getProjects.map((val, indice) =>
                  <tr key={indice} >
                    <td>
                      {val.name}
                    </td>
                    <td className="settings">
                      <iconsri.RiEdit2Line style={{ color: 'green' }}  onClick={()=>{triggerCreate(val.name)}} />
                      <iconsmd.MdDeleteOutline style={{ color: 'darkred' }} onClick={()=>{handleDelete(val.name)}}   />
                    </td>
                  </tr>
                )
              }
            </tbody>
          </Table>
        </CardBody>
      </Card>
      {
        ocultarCreateProj ? (
          <Card className='card-projects card-projects-right'>
            <CardHeader >
              <Button onClick={() => { setOculutarCreateProj(false) }}>X</Button>
            </CardHeader>
            <CardBody>
              <Table>
                <thead>
                  <tr>
                    <th>
                      Create New Project
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td className="td-project-create">
                      <Label for="name">
                        Name
                      </Label>
                      <Input type="text" value={name} onChange={handleNameUpdate} />
                      <FormFeedback className={resExist ? 'invalid-feedback' : 'invalid-feedback-hide'}>
                        Kubernetes '{name}'  already exist !
                      </FormFeedback>

                    </td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr>
                    <th className='btn-project'>
                      <Button onClick={triggerClickAdd}>+ add</Button>
                    </th>
                  </tr>
                </tfoot>
              </Table>
            </CardBody>
          </Card>
        ) : <></>//console.log("Nothing to do! 'ocultarCreateProj': " + ocultarCreateProj)
      }

      {
        triggerCreateConfig ? (
          <Card className='card-projects card-projects-right card-kubernetes-rigth' >
            <CardHeader>
              <div style={{float: 'left',width: '50%',textAlign: 'left',fontWeight: 'bold',color: 'gray'}}>
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
                      <div className="select-type-provider td-kubernetes" onClick={() => triggerChageLogoOP(getProjectName)}>
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
  )
}
