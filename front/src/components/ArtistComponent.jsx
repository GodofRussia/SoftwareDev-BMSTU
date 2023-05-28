import React, {useEffect, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const ArtistComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [name, setName] = useState("")
    const [century, setCentury] = useState("")
    const [id, setId] = useState(useParams().id)

    const updateName = (event) => {
        setName(event.target.value)
    }

    const updateCentury = (event) => {
        setCentury(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (!name){
            err = "Имя художника должно быть указано"
        }
        let artist = {name: name, id: id}
        if (parseInt(id) === -1) {
            artist.id = Math.floor(Math.random() * 100);
            BackendService.createArtist(artist)
                .catch(()=>{})
        }
        else {
            BackendService.updateArtist(artist)
                .catch(()=>{})
        }
        navigateToArtists();
    }

    const navigateToArtists = () => {
        navigate('/artists');
    }

    const refreshArtist = () => {
        BackendService.retrieveArtist(id)
            .then(
                resp => {
                    setName(resp.data.name);
                    setCentury(resp.data.century);
                    setHidden(false);
                }
            )
            .catch(()=> {
                setHidden(true);
            });
    }

    useEffect(() => {
        refreshArtist();
    });

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Художник</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={() => navigateToArtists() }><FontAwesomeIcon
                    icon={faChevronLeft}/>{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Имя</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите имя художника"
                        onChange={updateName}
                        value={name}
                        name="name"
                        autoComplete="off"/>

                    <Form.Label>Век</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите век жизни художника"
                        onChange={updateCentury}
                        value={century}
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

export default ArtistComponent;
