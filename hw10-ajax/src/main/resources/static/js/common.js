function getBookIdFromUrl() {
    const pathSegments = window.location.pathname.split('/');
    return pathSegments[pathSegments.length - 1];
}

async function makeRequest(url, options = {}) {
    const response = await fetch(url, options);
    console.log(url)
    if (response.ok) {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return response;
        }
        return null;
    } else {
        let errorMessage = 'An error occurred';
        try {
            const errorData = await response.json();
            errorMessage = errorData.message || errorMessage;
        } catch {
            errorMessage = `HTTP error! status: ${response.status}`;
        }

        alert(errorMessage);
        window.location.href = "/home";

        throw new Error(errorMessage);
    }
}