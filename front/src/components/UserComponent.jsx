import React, {useEffect, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const UserComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [login, setLogin] = useState("")
    const [email, setEmail] = useState("")
    const [id, setId] = useState(useParams().id)

    const updateLogin = (event) => {
        setLogin(event.target.value)
    }
    const updateEmail = (event) => {
        setEmail(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (login === ""){
            err = "Название музея должно быть указано"
        }
        let user = {login: login, email: email, id: id}
        if (parseInt(id) === -1) {
            BackendService.createUser(user)
                .catch(()=>{})
        }
        else {
            BackendService.updateUser(user)
                .catch(()=>{})
        }
        navigateToUsers();
    }

    const navigateToUsers= () => {
        navigate('/users')
    }

    const refreshUser = () => {
        BackendService.retrieveUser(id)
            .then(
                resp => {
                    setLogin(resp.data.login);
                    setEmail(resp.data.email);
                    setHidden(false);
                }
            )
            .catch(()=> {
                setHidden(true);
            });
    }

    useEffect(() => {
        refreshUser();
    });

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Пользователь</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={()=> navigateToUsers() }><FontAwesomeIcon
                    icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Логин</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите логин"
                        onChange={updateLogin}
                        value={login}
                        name="name"
                        autoComplete="off"/>

                    <Form.Label>Пароль</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите почту"
                        onChange={updateEmail}
                        value={email}
                        name="name"
                        autoComplete="off"/>
                </Form.Group>

                <button
                    className="btn btn-outline-secondary"
                    type="submit"><FontAwesomeIcon
                    icon={faSave}/>{' '}Сохранить</button>
            </Form>
        </div>
    )

}

export default UserComponent;
