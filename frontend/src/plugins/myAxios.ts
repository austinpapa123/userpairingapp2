import axios from "axios";

// Set config defaults when creating the instance
const myAxios = axios.create({
    baseURL: 'http://localhost:8080/signal/',
    withCredentials: true,
});

// Add a request interceptor
myAxios.interceptors.request.use(function (config: any) {
    // Do something before request is sent
    //console.log('bout to send request')
    return config;
}, function (error: any) {
    // Do something with request error
    console.log('request not ok')
    return error;
});

// Add a response interceptor
myAxios.interceptors.response.use(function (response: any) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    //console.log('bout to receive respond')
    return response.data;
}, function (error: any) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    console.log('response not ok')
    console.log("error", error);
    return error;
});

export default myAxios;