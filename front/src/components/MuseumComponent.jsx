import React, {useEffect, useId, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';
import {connect, useDispatch} from "react-redux";
import {alertActions} from "../utils/Rdx";

const MuseumComponent = props => {

    const [Name, setName] = useState([]);
    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const nameId = useId();
    const params = useParams();
    const [Location, setLocation] = useState("")
    const [id, setId] = useState(useParams().id)

    const loadCountry = () => {
        if (params.id === String(-1)) {
            params.id = (Math.floor(Math.random() * 91) + 10);
        }
        BackendService.retrieveMuseum(params.id)
            .then(
                resp => {
                    setName(resp.data.name);
                    setHidden(false )
                })
            .catch(()=> { setHidden(true )})
    }

    useEffect(() => {
        if (parseInt(params.id) != -1) {
            loadCountry();
        }
    }, [])

    const onSubmit = e => {
        e.preventDefault();
        const form = e.target;
        const formData = new FormData(form);
        const formJson = Object.fromEntries(formData.entries());
        let err = null;
        if (!formJson.name) {
            err = "Название музея должно быть указано";
        }
        if (err) {
            dispatch(alertActions.error(err))
        }
        let country = { id: params.id, name: formJson.name, location: formJson.location};
        if (parseInt(country.id) === -1) {
            BackendService.createMuseum(country)
                .then(()=> navigate("/museums"))
                .catch(()=> {})
        }
        else {
            BackendService.updateMuseum(country)
                .then(()=> navigate("/museums"))
                .catch(()=>{})
        }
    }

    if (hidden)
        return null;
    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <h3>Страна</h3>
                </div>
                <div className="col-md-6 clearfix">
                    <button className="btn btn-outline-secondary float-end"
                            onClick={ () => { navigate(-1) } }>
                        <FontAwesomeIcon icon={faChevronLeft}/>{' '}Назад</button>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <form method="post" onSubmit={onSubmit}>
                        <label className="form-label" htmlFor={nameId}>Название:</label>
                        <input id={nameId} name="name"
                               className="form-control"
                               defaultValue={Name}
                               autoComplete="off"/>
                        <label className="form-label">Локация:</label>
                        <input name="location"
                               className="form-control"
                               defaultValue={Location}
                               autoComplete="off"/>
                        <button
                            className="btn btn-outline-secondary mt-4"
                            type="submit">
                            <FontAwesomeIcon icon={faSave}/>{' '}Сохранить</button>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default connect()(MuseumComponent);

