import { API_BASE_URL, ACCESS_TOKEN } from '../constants';
import {fetchEditEpi} from './APIAdmin';

const request = (options) => {
    const headers = new Headers({
        'Content-Type' : 'application/json',
    })

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};


const deleteRequest = (options) => {
    const headers = new Headers()

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
}


export function login(loginRequest) {
    return request({
        url: "/user-service/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: "/user-service/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}


export function checkUsernameAvailability(username) {
    return request({
        url: "/user-service/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: "/user-service/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return request({
        url: "/user-service/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: "/user-service/user/" + username,
        method: 'GET'
    });
}

export function uploadComment(id, username, comment){
    const formData = new FormData();
    formData.append('user', username);
    formData.append('comment', comment);
    return request({
        url: "/webtoon-service/saveComment/" + id,
        method: 'POST',
        body: formData
    });
}


///


// 캐시 충전
export function chargeCash(cash){
    var data = JSON.stringify(
        { "change_amount" : cash,
            "toon_name" : "",
            "epi_name": "",
            "epi_no": "",
            "content" : "CHARGE"
        }
    )
    return request({
        contentType: "application/json",
        url:  "/cash-service/pushmongo",
        method: 'POST',
        body: data
    });
}

// 캐시충전-캐시수정
export function chargeCash_user(cash){
    var data = JSON.stringify(
        { "cash_amount" : cash
        }
    )
    return request({
        contentType: "application/json",
        url:  "/user-service/chargeCash",
        method: 'POST',
        body: data
    });
}



// 대여권 조회
export function CheckRentIng(){
    return request({
        url : "/cash-service/CheckRentIng",
        method: 'GET'
    });
}

// 자기 캐시 조회
export function checkCash(){
    return request({
        url : "/user-service/CheckCash/me",
        method: 'GET'
    });
}

// 캐시 내역 조회
export function getCashRecord(){
    return request({
        url : "/cash-service/getmongo",
        method: 'GET'
    });
}


// 해당 에피소드의 대여권이 있는지
export function checkEpiRent(id){
    return request({
        url : "/cash-service/CheckRentEpi/" + id,
        method: 'GET'
    });
}


// 대여권구매-대여권기록
export function rentEpisode(epiid, epiTitle, webtoonTitle){
    var data =JSON.stringify(
        { "change_amount" : "10",
            "toon_name" : webtoonTitle,
            "epi_name": epiTitle,
            "epi_no": epiid,
            "content" : "RENT_WEBTOON"
        })
    return request({
        contentType: "application/json",
        url: "/cash-service/pushmongo",
        method: 'POST',
        body: data
    })
}


// 대여권구매-캐시수정
export function rentToon(){
    return request({
        url : "/user-service/rentToon",
        method: 'GET'
    });
}







export function getComment(id){
    return request({
        url : "/webtoon-service/getComment/" + id,
        method: 'GET'
    });
}

export function deleteComment(id) {
    return deleteRequest({
        url: "/webtoon-service/deleteComment/" + id,
        method: 'DELETE'
    });
}


export function fetchRate(id, user){
    return request({
        url : "/webtoon-service/fetchRate/" + id + "/" + user,
        method: 'GET'
    });
}



export function getAvgRate(id){
    return request({
        url : "/webtoon-service/getAvgRate/" + id,
        method: 'GET'
    });
}


export function getFalse(){
    return request({
        url : "/alwaysGetFalse",
        method: 'GET'
    });
}

export function getTrue(){
    return request({
        url : "/alwaysGetTrue",
        method: 'GET'
    });
}
