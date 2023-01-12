import React, { Component, useEffect, useState } from 'react'
import { Button as Boton, Button, Card, CardBody, FormGroup } from 'reactstrap';
import '../estilos/paginas/shellPage.scss'

function ShellPage() {
  const [result, setResult] = useState("~");
  const [configs, setConfig] = useState([{}]);
  const [requestValues, setRequestValues] = useState({});
  const putCongs = async (event) => {
    const head = {
      method: "put",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(event)
    }
    let configname = requestValues.configname;
    let hostname = requestValues.host;
    const urlEx1 = process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + configname + "/" + hostname + "/shell/exec";
    const response = await fetch(urlEx1, head);

    const data = await response.json();
    setResult(data.resultOutPut);
    console.log(data);

  };

  useEffect(
    () => {
      if (configs.length === 1) {
        console.log(configs.length)
      }
      const getConfigs = async () => {
        try {
          let base_uri = process.env.REACT_APP_BACKEND_BASE_URI
          const response = await fetch(base_uri + '/api/v1/kubernetes/hosts');
          const data = await response.json();
          console.log(data);
          setConfig(data);
        } catch (err) {
          setConfig([{}]);
        }
      };
      getConfigs();
    }, []);


  const handleScriptConsole = (event) => {
    let event1 = {
      "cmd": event + ""
    };
    console.log(event1)
    putCongs(event1);
  }

  const [cmd, setCmd] = useState("");
  const handleScriptConsole2 = (event) => {
    setCmd(event.target.value)
  }

  const [kubernetes, setKubernetes] = useState("KUBERNETES");

  const handleKubernetes = (res) => {
    let split_res = res.target.value.split("/");
    let values = {
      configname: split_res[1],
      host: split_res[2]
    }
    setRequestValues(values);
    setKubernetes(res.target.value);
  }


  return (
    <>
      <Card className='shellpage' >
        <CardBody>
          <FormGroup className='formgroup0'>
            <select value={kubernetes + ""} onChange={evento => handleKubernetes(evento)} >
              {
                configs.map((val) =>
                  <option value={val.projectname + "/" + val.configname + "/" + val.host}>{val.projectname}/{val.configname}/{val.host}</option>
                )
              }
            </select>


            <Boton onClick={() => handleScriptConsole(cmd)}>enter</Boton>
          </FormGroup>

          <FormGroup className='formgroup1'>
            <textarea onChange={event => handleScriptConsole2(event)} name="shelllogs" />
          </FormGroup>

          <FormGroup className='formgroup2'>
            <textarea value={result} name="shellcommand" >{result}</textarea>
          </FormGroup>
          <FormGroup className='apps'>
            <br></br>
            <br></br>
            APPLICATIONS
            <br></br>
            <br></br>helloworld.yaml <Button value="">Upload file</Button> <Button value="">Deploy</Button>
          </FormGroup>

        </CardBody>
      </Card>

    </>
  )
}



export default ShellPage