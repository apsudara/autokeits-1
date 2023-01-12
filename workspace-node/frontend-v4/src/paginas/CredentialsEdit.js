import React from 'react'
import '../estilos/paginas/CredentialsEdit.scss'
import { Button, Card, Form, FormGroup, Input, Label } from 'reactstrap'

export default function CredentialsEdit() {
    return (
        <div className='credentialsEdit'>
            <Card>
                <Form>
                    <FormGroup className='formGroup'>
                        <Label>Name</Label>
                        <Input></Input>
                        <Label>User</Label>
                        <Input></Input>
                        <Label>Password</Label>
                        <Input></Input>
                    </FormGroup>
                </Form>
                <Button>create</Button>

            </Card>
        </div>
    )
}
