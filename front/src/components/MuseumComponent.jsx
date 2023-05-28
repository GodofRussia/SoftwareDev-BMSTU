import React, {useEffect, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const MuseumComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [name, setName] = useState("")
    const [location, setLocation] = useState("")
    const [id, setId] = useState(useParams().id)

    const updateName = (event) => {
        setName(event.target.value)
    }
    const updateLocation = (event) => {
        setLocation(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (name === ""){
            err = "Название музея должно быть указано"
        }
        let museum = {name: name, location: location, id: id}
        if (parseInt(id) === -1) {
            BackendService.createMuseum(museum)
                .catch(()=>{})
        }
        else {
            BackendService.updateMuseum(museum)
                .catch(()=>{})
        }
        navigateToPaintings()
    }

    const navigateToPaintings= () => {
        navigate('/paintings')
    }

    const refreshMuseum = () => {
        BackendService.retrieveMuseum(id)
            .then(
                resp => {
                    setName(resp.data.name);
                    setLocation(resp.data.location);
                    setHidden(false);
                }
            )
            .catch(()=> {
                setHidden(true);
            });
    }

    useEffect(() => {
        refreshMuseum();
    });

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Музей</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={()=>  navigateToPaintings() }><FontAwesomeIcon
                    icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Название</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите название картины"
                        onChange={updateName}
                        value={name}
                        name="name"
                        autoComplete="off"/>

                    <Form.Label>Локация</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите название музея"
                        onChange={updateLocation}
                        value={location}
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

export default MuseumComponent;
