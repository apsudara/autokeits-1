import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, CardHeader, Table, Label, Input, FormFeedback } from 'reactstrap';

import * as iconsri from "react-icons/ri"
import * as iconsmd from "react-icons/md"


export default function Credentials() {
  const [nameCred, setNameCred] = useState("");
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [typeCred, setTypeCred] = useState("");
  const updateNameCred = (credName) => {
    setNameCred(credName.target.value)
  }
  const updateUserName = (userName) => {
    setUserName(userName.target.value)
  }
  const updatePassword = (password) => {
    setPassword(password.target.value)
  }
  const updateTypeCred = (typeCred) => {
    setTypeCred(typeCred.target.value)
  }
  const dataCred = {
    "name": nameCred,
    "user": userName,
    "password": password,
    "type": typeCred
  }
  const [name, setName] = useState("");
  const [resExist, setResExist] = useState(false);
  const [getCredentials, setCredentials] = useState([]);
  useEffect(() => {
    // console.log('Triggered setCredentials! create ')
    const getProjs = async () => {
      try {
        const response = await fetch(process.env.REACT_APP_BACKEND_BASE_URI + '/api/v1/credentials');
        const data = await response.json();
        setCredentials(data);
      } catch (err) {
        setCredentials([]);
      }
    };
    getProjs();
  }, []); // ver como se actualiza las peticiones solamente cada vez que se haga un triguer en modificar datos 
  const [ocultarCreateCred, setOculutarCreateCred] = useState(false);
  const triggerClickCreate = () => {
    console.log('Click create project Triggered!');
    setOculutarCreateCred(true);
  }
  const triggerClickAdd = () => {
    const putCongs = async () => {
      const head = {
        method: "put",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(dataCred)
      }
      const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/credentials/" + name;
      try {
        const response = await fetch(urlEx1, head);
        const data = await response.json();
        setResExist(data.isError);
      } catch (error) {
        setResExist(true);
      }
    };
    if (!resExist) {
      putCongs();
      //navigate('/kubernetes');
    }

    //
  }
  const handleNameUpdate = (res) => {
    updateNameCred(res)
    const isNameExists = async () => {
      if (res.target.value !== "") {
        const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/credentials/" + res.target.value + "/exists";
        try {
          const response = await fetch(urlEx1);
          const data = await response.json();
          setResExist(data.isError);
        } catch (error) {
          setResExist(false);
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
        const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/credentials/" + res;
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






  return (
    <div className='projectPage'>
      <Card className='card-projects card-projects-left'>
        <CardHeader>
          Credentials
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
                // console.log( getCredentials.length);
                getCredentials.length > 0 ?
                  getCredentials.map((val, indice) =>
                    <tr key={indice} >
                      <td>
                        {val.name}
                      </td>
                      <td className="settings">
                        <iconsri.RiEdit2Line style={{ color: 'green' }} />
                        <iconsmd.MdDeleteOutline style={{ color: 'darkred' }} onClick={() => { handleDelete(val.name) }} />
                      </td>
                    </tr>
                  ) : <></>
              }
            </tbody>
          </Table>
        </CardBody>
      </Card>
      {
        ocultarCreateCred ? (
          <Card className='card-projects card-projects-right'>
            <CardHeader >
              <Button onClick={() => { setOculutarCreateCred(false) }}>X</Button>
            </CardHeader>
            <CardBody className='node-cols'>
              <Table>
                <thead>
                  <tr>
                    <th>
                      Create New Credentials
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>
                      <Label for="name">
                        Name:
                      </Label>
                      <Input type="text" value={nameCred} onChange={handleNameUpdate} />

                      <FormFeedback className={resExist ? 'invalid-feedback' : 'invalid-feedback-hide'}>
                        Kubernetes '{nameCred}'  already exist !
                      </FormFeedback>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <Label for="user">
                        User:
                      </Label>
                      <Input type="text" value={userName} onChange={updateUserName} />
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <Label for="password">
                        Password:
                      </Label>
                      <Input type="text" value={password} onChange={updatePassword} />

                    </td>
                  </tr>
                  {/* <tr>
                    <td>
                      <Label for="type">
                        Type: 
                      </Label>
                      <Input type="text" value={typeCred} onChange={updateTypeCred} />
                    </td>
                  </tr> */}

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
        ) : <></>//console.log("Nothing to do! 'ocultarCreateCred': " + ocultarCreateCred)
      }
    </div>
  )
}
