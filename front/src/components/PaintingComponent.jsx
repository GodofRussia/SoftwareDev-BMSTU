import React, {useEffect, useId, useState} from "react";
import BackendService from "../services/BackendService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import{faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {Form} from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';
import {connect, useDispatch} from "react-redux";
import {alertActions} from "../utils/Rdx";


const PaintingComponent = props => {

    const [Name, setName] = useState([]);
    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const nameId = useId();
    const params = useParams();
    const [Year, setYear] = useState("")
    const [id, setId] = useState(useParams().id)

    const loadCountry = () => {
        if (params.id === String(-1)) {
            params.id = (Math.floor(Math.random() * 91) + 10);
        }
        BackendService.retrievePainting(params.id)
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
            err = "Название картины должно быть указано";
        }
        if (err) {
            dispatch(alertActions.error(err))
        }
        let painting = { id: params.id, name: formJson.name, artist: {id: formJson.artist}};
        if (parseInt(painting.id) === -1) {
            BackendService.createPainting(country)
                .then(()=> navigate("/museums"))
                .catch(()=> {})
        }
        else {
            BackendService.updatePainting(country)
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
                        <label className="form-label">Год:</label>
                        <input name="year"
                               className="form-control"
                               defaultValue={Year}
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

export default connect()(PaintingComponent);
