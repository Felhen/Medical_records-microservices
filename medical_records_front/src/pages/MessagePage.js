import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthProvider';

export default function MessagePage() {
    const { userId, userRole } = useAuth();
    const [messages, setMessages] = useState([]);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const response = await axios.get(`http://localhost:8082/${userId}/inbox`);
                setMessages(response.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };


        const fetchUserList = async () => {
            try {
                const userResponse = await axios.get('http://localhost:8080/users');
                setUsers(userResponse.data);
            } catch (error) {
                console.error('Error fetching staff list:', error);
            }
        };

        fetchMessages();
        fetchUserList();
    }, [userId]);

    // Function to get sender's username by ID
    const getSenderUsername = (senderId) => {
        const sender = users.find(user => user.id === senderId);
        return sender ? sender.username : 'Unknown';
    };

    return (
        <div className='container'>
            <div className='py-4'>
                {loading ? (
                    <p>Loading messages...</p>
                ) : (
                    <div>
                        {userRole === 'PATIENT' && (
                            <div>
                                <h2>Doctors/Staff List</h2>
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th scope="col">#</th>
                                            <th scope="col">Username</th>
                                            <th scope="col">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {users
                                            .filter((user) => user.role !== 'PATIENT')
                                            .map((user, index) => (
                                                <tr key={user.id}>
                                                    <th scope="row">{index + 1}</th>
                                                    <td>{user.username}</td>
                                                    <td>
                                                        <Link to={`/messages/new_message/${user.id}`} className='btn btn-primary mx-2'>New Message</Link>
                                                    </td>
                                                </tr>
                                            ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                        {/* Display the user's messages */}
                        <h2>Inbox</h2>
                        {messages.map((message) => (
                            <div key={message.id}>
                                <p>From: {getSenderUsername(message.sender)}</p>
                                <p>{message.content}</p>
                                {/* Link to respond with the sender's ID as a parameter */}
                                {userRole !== 'PATIENT' && (
                                    <Link to={`/messages/new_message/${message.sender}`} className='btn btn-primary mx-2'>Respond</Link>
                                )}
                                <hr />
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
    
}

