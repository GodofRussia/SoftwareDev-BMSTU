import React, {useEffect, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const CountryComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [name, setName] = useState("")
    const [id, setId] = useState(useParams().id)

    const updateName = (event) => {
        setName(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (name === ""){
            err = "Название страны должно быть указано"
        }
        let countr = {name: name, id: id}
        if (parseInt(id) === -1) {
            countr.id = Math.floor(Math.random() * 100);
            BackendService.createCountry(countr)
                .catch(()=>{})
        }
        else {
            BackendService.updateCountry(countr)
                .catch(()=>{})
        }
        navigateToCountries()
    }

    const refreshCountry = () => {
        BackendService.retrieveCountry(id)
            .then(
                resp => {
                    setName(resp.data.name);
                    setHidden(false);
                }
            )
            .catch(()=> {
                setHidden(true);
            });
    }

    useEffect(() => {
        refreshCountry();
    });

    const navigateToCountries = () => {
        navigate('/countries')
    }

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Страна</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={() => navigateToCountries() }><FontAwesomeIcon
                    icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Название</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите название страны"
                        onChange={updateName}
                        value={name}
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

export default CountryComponent;
