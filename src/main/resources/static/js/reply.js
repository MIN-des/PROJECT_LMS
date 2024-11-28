async function get1(bno) { // get1()은 비동기 함수임, read.html에서 promiss 출력됨
    const result = await axios.get(`/replies/list/${bno}`)

    return result.data // Axios 호출 결과 리턴받아야 함
}

async function getList({bno, page, size, goLast}) {
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if(goLast) {
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total / size))
    }

    return result.data
}

async function addReply(replyObj) { // 정상 동작 시 rno:11과 같은 JSON 데이터 전송
    const response = await axios.post(`/replies/`, replyObj)

    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)

    return response.data
}

async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)

    return response.data
}