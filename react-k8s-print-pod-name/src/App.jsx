import { useState } from "react";

export default function App() {
  const podName = import.meta.env.VITE_POD_NAME || "Pod name not set";
  const apiServer = import.meta.env.VITE_API_SERVER;
  const [apiPodName, setApiPodName] = useState("");
  const [loadingPodName, setLoadingPodName] = useState(false);
  const [errorPodName, setErrorPodName] = useState("");

  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [errorUsers, setErrorUsers] = useState("");

  const fetchApiPodName = async () => {
    setLoadingPodName(true);
    setErrorPodName("");
    try {
      const response = await fetch(apiServer + "/podname");
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.text();
      setApiPodName(data);
    } catch (err) {
      setErrorPodName("Failed to fetch API pod name.");
    } finally {
      setLoadingPodName(false);
    }
  };

  const fetchUsers = async () => {
    setLoadingUsers(true);
    setErrorUsers("");
    try {
      const response = await fetch(apiServer + "/users");
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const data = await response.json();
      setUsers(data);
    } catch (err) {
      setErrorUsers("Failed to fetch user data.");
    } finally {
      setLoadingUsers(false);
    }
  };

  return (
    <div className="fixed inset-0 flex flex-col items-center justify-center bg-white">
      <div className="bg-blue-500 text-white text-3xl font-bold py-4 px-8 rounded-lg shadow-lg mb-4">
        React server is up and runing on pod: {podName}
      </div>
      <button
        onClick={fetchApiPodName}
        disabled={loadingPodName}
        className={`bg-green-500 text-white font-semibold py-2 px-4 rounded-lg shadow-lg hover:bg-green-600 ${
          loadingPodName ? "opacity-50 cursor-not-allowed" : ""
        }`}
      >
        {loadingPodName ? "Loading..." : "Get API Server"}
      </button>
      {apiPodName && (
        <div className="bg-blue-500 text-white text-3xl font-bold py-4 px-8 rounded-lg shadow-lg mt-4">
          {apiPodName}
        </div>
      )}
      {errorPodName && (
        <div className="text-red-500 font-semibold mt-4">{errorPodName}</div>
      )}

      <button
        onClick={fetchUsers}
        disabled={loadingUsers}
        className={`bg-purple-500 text-white font-semibold py-2 px-4 rounded-lg shadow-lg hover:bg-purple-600 mt-4 ${
          loadingUsers ? "opacity-50 cursor-not-allowed" : ""
        }`}
      >
        {loadingUsers ? "Loading..." : "Get Data from Database"}
      </button>
      {errorUsers && (
        <div className="text-red-500 font-semibold mt-4">{errorUsers}</div>
      )}

      {users.length > 0 && (
        <table className="table-auto border-collapse border border-gray-400 mt-4">
          <thead>
            <tr className="bg-gray-200">
              <th className="border border-gray-400 px-4 py-2">ID</th>
              <th className="border border-gray-400 px-4 py-2">Username</th>
              <th className="border border-gray-400 px-4 py-2">
                Mobile Number
              </th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="hover:bg-gray-100">
                <td className="border border-gray-400 px-4 py-2">{user.id}</td>
                <td className="border border-gray-400 px-4 py-2">
                  {user.username}
                </td>
                <td className="border border-gray-400 px-4 py-2">
                  {user.mobileNumber}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
