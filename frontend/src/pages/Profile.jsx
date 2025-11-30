import React, { useState, useEffect } from 'react';
import { PieChart, Pie, Cell, Legend } from "recharts";

function Profile() {
  const [user, setUser] = useState({
    userId: null,
    name: '',
    email: '',
    diet_type: '',
    calorieGoal: 2000,
    proteinGoal: 150,
    carbsGoal: 200,
    fatsGoal: 65
  });

  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [mealPlans, setMealPlans] = useState([]);
  const [isEditingGoals, setIsEditingGoals] = useState(false);
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [isChangingPassword, setIsChangingPassword] = useState(false);
  const [tempGoals, setTempGoals] = useState({});
  const [tempProfile, setTempProfile] = useState({});
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log('🚀 Profile component mounted');
    console.log('📦 All localStorage keys:', Object.keys(localStorage));
    console.log('👾 userId:', localStorage.getItem('userId'));
    console.log('👾 userName:', localStorage.getItem('userName'));

    loadUserData();
    loadMealPlans();
  }, []);

  const loadUserData = async () => {
    console.log('🔍 Starting to load user data...');

    const userId = localStorage.getItem('userId');
    console.log('👤 User ID from localStorage:', userId);

    if (!userId) {
      console.error('❌ No userId found in localStorage');
      setError('No user logged in');
      setLoading(false);
      return;
    }

    try {
      const apiUrl = `http://localhost:8080/api/users/${userId}`;
      console.log(`🌐 Fetching user from API: ${apiUrl}`);

      const response = await fetch(apiUrl);
      console.log('📡 User API Response status:', response.status);

      if (response.ok) {
        const userData = await response.json();
        console.log('✅ API returned user data:', userData);

        setUser(prev => ({
          ...prev,
          userId: userData.userId || userId,
          name: userData.name || 'User',
          email: userData.email || '',
          diet_type: userData.diet_type || 'none'
        }));
      }

      const goalsUrl = `http://localhost:8080/api/health-goals?userId=${userId}`;
      console.log(`🎯 Fetching health goals from API: ${goalsUrl}`);

      const goalsResponse = await fetch(goalsUrl);
      console.log('📡 Health Goals API Response status:', goalsResponse.status);

      if (goalsResponse.ok) {
        const goalsData = await goalsResponse.json();
        console.log('✅ API returned health goals:', goalsData);

        setUser(prev => ({
          ...prev,
          calorieGoal: goalsData.calorieGoal || 2000,
          proteinGoal: goalsData.proteinGoal || 150,
          carbsGoal: goalsData.carbsGoal || 200,
          fatsGoal: goalsData.fatsGoal || 65
        }));
      }

      console.log('✅ Data loading complete');

    } catch (err) {
      console.error('❌ Error loading user data:', err);
      setError(err.message);
    } finally {
      console.log('🏁 Setting loading to false');
      setLoading(false);
    }
  };

  const loadMealPlans = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/mealplans');
      const data = await response.json();
      setMealPlans(data);
      console.log('✅ Loaded meal plans:', data.length);
    } catch (err) {
      console.error('❌ Error fetching meal plans:', err);
    }
  };

  const COLORS = ["#2f855a", "#3182ce", "#d69e2e", "#e53e3e"];

  const getNutritionData = () => [
    { name: "Calories", value: user.calorieGoal },
    { name: "Protein (g)", value: user.proteinGoal },
    { name: "Carbs (g)", value: user.carbsGoal },
    { name: "Fats (g)", value: user.fatsGoal },
  ];

  const handleEditProfile = () => {
    setTempProfile({
      name: user.name,
      email: user.email,
      diet_type: user.diet_type
    });
    setIsEditingProfile(true);
  };

  const handleSaveProfile = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/${user.userId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(tempProfile)
      });

      if (!response.ok) {
        throw new Error('Failed to update profile');
      }

      const result = await response.json();

      setUser(prev => ({
        ...prev,
        name: tempProfile.name,
        email: tempProfile.email,
        diet_type: tempProfile.diet_type
      }));

      localStorage.setItem('user', JSON.stringify({
        userId: user.userId,
        name: tempProfile.name,
        email: tempProfile.email,
        diet_type: tempProfile.diet_type
      }));

      setIsEditingProfile(false);
      alert('✅ Profile updated successfully!');
    } catch (err) {
      console.error('❌ Error updating profile:', err);
      alert('Failed to update profile. Please try again.');
    }
  };

  const handleCancelEditProfile = () => {
    setIsEditingProfile(false);
    setTempProfile({});
  };

  const handleEditGoals = () => {
    setTempGoals({
      calorieGoal: user.calorieGoal,
      proteinGoal: user.proteinGoal,
      carbsGoal: user.carbsGoal,
      fatsGoal: user.fatsGoal
    });
    setIsEditingGoals(true);
  };

  const handleSaveGoals = async () => {
    const newGoals = {
      calorieGoal: parseInt(tempGoals.calorieGoal) || user.calorieGoal,
      proteinGoal: parseFloat(tempGoals.proteinGoal) || user.proteinGoal,
      carbsGoal: parseFloat(tempGoals.carbsGoal) || user.carbsGoal,
      fatsGoal: parseFloat(tempGoals.fatsGoal) || user.fatsGoal
    };

    try {
      const response = await fetch('http://localhost:8080/api/health-goals', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId: user.userId.toString(),
          calorieGoal: newGoals.calorieGoal.toString(),
          proteinGoal: newGoals.proteinGoal.toString(),
          carbsGoal: newGoals.carbsGoal.toString(),
          fatsGoal: newGoals.fatsGoal.toString(),
          goalType: 'custom'
        })
      });
      console.log('📡 Response status:', response.status);  
      console.log('📡 Response OK?:', response.ok);         

      const responseText = await response.text();          
      console.log('📡 Response body:', responseText);      


      if (!response.ok) {
        throw new Error('Failed to save goals to database');
      }

      const result = JSON.parse(responseText);             
      console.log('✅ Saved health goals to database:', result);

      setUser(prev => ({ ...prev, ...newGoals }));

      localStorage.setItem('nutritionGoals', JSON.stringify(newGoals));

      setIsEditingGoals(false);
      alert('✅ Nutrition goals saved successfully!');
    } catch (err) {
      console.error('❌ Error saving goals:', err);

      setUser(prev => ({ ...prev, ...newGoals }));
      localStorage.setItem('nutritionGoals', JSON.stringify(newGoals));
      setIsEditingGoals(false);
      alert('⚠️ Goals saved locally (database update failed)');
    }
  };

  const handleCancelEditGoals = () => {
    setIsEditingGoals(false);
    setTempGoals({});
  };

  const handleChangePassword = async () => {
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      alert('❌ New passwords do not match!');
      return;
    }

    if (passwordData.newPassword.length < 6) {
      alert('❌ Password must be at least 6 characters long!');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/users/change-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId: user.userId.toString(),
          currentPassword: passwordData.currentPassword,
          newPassword: passwordData.newPassword
        })
      });

      const result = await response.json();

      if (!response.ok) {
        throw new Error(result.error || 'Failed to change password');
      }

      alert('✅ Password changed successfully!');
      setIsChangingPassword(false);
      setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      console.error('❌ Error changing password:', err);
      alert(`Failed to change password: ${err.message}`);
    }
  };

  const getDaysInMonth = (date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    const days = [];
    for (let i = 0; i < firstDay; i++) {
      days.push(null);
    }
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(i);
    }
    return days;
  };

  const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'];

  const previousMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  const hasMealPlan = (day) => {
    const dateStr = `${currentMonth.getFullYear()}-${String(currentMonth.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return mealPlans.some(plan => {
      const startDate = new Date(plan.startDate).toISOString().split('T')[0];
      const endDate = new Date(plan.endDate).toISOString().split('T')[0];
      return dateStr >= startDate && dateStr <= endDate;
    });
  };

  if (loading) {
    return (
      <div style={{ padding: '2rem', textAlign: 'center' }}>
        <p>Loading profile...</p>
      </div>
    );
  }

  if (error && !user.userId) {
    return (
      <div style={{ padding: '2rem', textAlign: 'center' }}>
        <p style={{ color: '#e53e3e' }}>Error: {error}</p>
        <p>Please log in again.</p>
      </div>
    );
  }

  return (
    <div style={{ padding: '2rem', maxWidth: '1400px', margin: '0 auto' }}>
      <h1 style={{ marginBottom: '2rem', fontSize: '2rem', fontWeight: 'bold' }}>Profile</h1>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '2rem', marginBottom: '2rem' }}>

        {/* User Info Card */}
        <div style={{ backgroundColor: 'white', padding: '2rem', borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Personal Information</h2>
            {!isEditingProfile ? (
              <button
                onClick={handleEditProfile}
                style={{ padding: '0.5rem 1rem', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
              >
                Edit Profile
              </button>
            ) : (
              <div style={{ display: 'flex', gap: '0.5rem' }}>
                <button
                  onClick={handleSaveProfile}
                  style={{ padding: '0.5rem 1rem', backgroundColor: '#2f855a', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                >
                  Save
                </button>
                <button
                  onClick={handleCancelEditProfile}
                  style={{ padding: '0.5rem 1rem', backgroundColor: '#718096', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                >
                  Cancel
                </button>
              </div>
            )}
          </div>

          {isEditingProfile ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Name</label>
                <input
                  type="text"
                  value={tempProfile.name}
                  onChange={(e) => setTempProfile({ ...tempProfile, name: e.target.value })}
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Email</label>
                <input
                  type="email"
                  value={tempProfile.email}
                  onChange={(e) => setTempProfile({ ...tempProfile, email: e.target.value })}
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Diet Type</label>
                <select
                  value={tempProfile.diet_type}
                  onChange={(e) => setTempProfile({ ...tempProfile, diet_type: e.target.value })}
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                >
                  <option value="none">None</option>
                  <option value="vegetarian">Vegetarian</option>
                  <option value="vegan">Vegan</option>
                  <option value="pescatarian">Pescatarian</option>
                  <option value="keto">Keto</option>
                  <option value="paleo">Paleo</option>
                </select>
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', color: '#718096', fontSize: '0.875rem', marginBottom: '0.25rem' }}>Name</label>
                <p style={{ fontSize: '1.125rem', fontWeight: '500' }}>{user.name || 'Not set'}</p>
              </div>
              <div>
                <label style={{ display: 'block', color: '#718096', fontSize: '0.875rem', marginBottom: '0.25rem' }}>Email</label>
                <p style={{ fontSize: '1.125rem', fontWeight: '500' }}>{user.email || 'Not set'}</p>
              </div>
              <div>
                <label style={{ display: 'block', color: '#718096', fontSize: '0.875rem', marginBottom: '0.25rem' }}>Diet Type</label>
                <span style={{
                  display: 'inline-block',
                  padding: '0.25rem 0.75rem',
                  backgroundColor: '#ebf8ff',
                  color: '#2c5282',
                  borderRadius: '999px',
                  fontSize: '0.875rem',
                  fontWeight: '600'
                }}>
                  {user.diet_type || 'none'}
                </span>
              </div>
            </div>
          )}

          {/* Password Change Section */}
          <div style={{ marginTop: '2rem', paddingTop: '2rem', borderTop: '1px solid #e2e8f0' }}>
            {!isChangingPassword ? (
              <button
                onClick={() => setIsChangingPassword(true)}
                style={{ padding: '0.5rem 1rem', backgroundColor: '#ed8936', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
              >
                Change Password
              </button>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '0.5rem' }}>Change Password</h3>
                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Current Password</label>
                  <input
                    type="password"
                    value={passwordData.currentPassword}
                    onChange={(e) => setPasswordData({ ...passwordData, currentPassword: e.target.value })}
                    style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                  />
                </div>
                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>New Password</label>
                  <input
                    type="password"
                    value={passwordData.newPassword}
                    onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                    style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                  />
                </div>
                <div>
                  <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Confirm New Password</label>
                  <input
                    type="password"
                    value={passwordData.confirmPassword}
                    onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
                    style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                  />
                </div>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <button
                    onClick={handleChangePassword}
                    style={{ padding: '0.5rem 1rem', backgroundColor: '#2f855a', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                  >
                    Save Password
                  </button>
                  <button
                    onClick={() => {
                      setIsChangingPassword(false);
                      setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
                    }}
                    style={{ padding: '0.5rem 1rem', backgroundColor: '#718096', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                  >
                    Cancel
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Nutrition Goals Card */}
        <div style={{ backgroundColor: 'white', padding: '2rem', borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Nutrition Goals</h2>
            {!isEditingGoals ? (
              <button
                onClick={handleEditGoals}
                style={{ padding: '0.5rem 1rem', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
              >
                Edit Goals
              </button>
            ) : (
              <div style={{ display: 'flex', gap: '0.5rem' }}>
                <button
                  onClick={handleSaveGoals}
                  style={{ padding: '0.5rem 1rem', backgroundColor: '#2f855a', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                >
                  Save
                </button>
                <button
                  onClick={handleCancelEditGoals}
                  style={{ padding: '0.5rem 1rem', backgroundColor: '#718096', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                >
                  Cancel
                </button>
              </div>
            )}
          </div>

          {isEditingGoals ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Daily Calories Goal</label>
                <input
                  type="number"
                  value={tempGoals.calorieGoal}
                  onChange={(e) => setTempGoals({ ...tempGoals, calorieGoal: e.target.value })}
                  placeholder="2000"
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Daily Protein Goal (g)</label>
                <input
                  type="number"
                  value={tempGoals.proteinGoal}
                  onChange={(e) => setTempGoals({ ...tempGoals, proteinGoal: e.target.value })}
                  placeholder="150"
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Daily Carbs Goal (g)</label>
                <input
                  type="number"
                  value={tempGoals.carbsGoal}
                  onChange={(e) => setTempGoals({ ...tempGoals, carbsGoal: e.target.value })}
                  placeholder="200"
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div>
                <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '600' }}>Daily Fats Goal (g)</label>
                <input
                  type="number"
                  value={tempGoals.fatsGoal}
                  onChange={(e) => setTempGoals({ ...tempGoals, fatsGoal: e.target.value })}
                  placeholder="65"
                  style={{ width: '100%', padding: '0.5rem', border: '1px solid #e2e8f0', borderRadius: '6px' }}
                />
              </div>
              <div style={{ backgroundColor: '#f7fafc', padding: '1rem', borderRadius: '6px', marginTop: '1rem' }}>
                <p style={{ fontWeight: '600', marginBottom: '0.5rem' }}>Weekly Goals Preview:</p>
                <p>Calories: {(parseInt(tempGoals.calorieGoal) || user.calorieGoal) * 7} kcal</p>
                <p>Protein: {(parseInt(tempGoals.proteinGoal) || user.proteinGoal) * 7}g</p>
                <p>Carbs: {(parseInt(tempGoals.carbsGoal) || user.carbsGoal) * 7}g</p>
                <p>Fats: {(parseInt(tempGoals.fatsGoal) || user.fatsGoal) * 7}g</p>
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
              {/* Daily Goals Chart */}
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '1rem' }}>Daily Nutrition Goals</h3>
                <PieChart width={320} height={320}>
                  <Pie
                    data={getNutritionData()}
                    cx={160}
                    cy={140}
                    innerRadius={60}
                    outerRadius={100}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {COLORS.map((color, index) => (
                      <Cell key={`cell-${index}`} fill={color} />
                    ))}
                  </Pie>
                  <Legend />
                </PieChart>
                <div style={{ marginTop: '1.5rem', backgroundColor: '#f7fafc', padding: '1.5rem', borderRadius: '8px' }}>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', textAlign: 'left' }}>
                    <div>
                      <p style={{ fontSize: '0.875rem', color: '#718096', marginBottom: '0.25rem' }}>Calories</p>
                      <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#2f855a' }}>{user.calorieGoal}</p>
                      <p style={{ fontSize: '0.75rem', color: '#718096' }}>kcal/day</p>
                    </div>
                    <div>
                      <p style={{ fontSize: '0.875rem', color: '#718096', marginBottom: '0.25rem' }}>Protein</p>
                      <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#3182ce' }}>{user.proteinGoal}</p>
                      <p style={{ fontSize: '0.75rem', color: '#718096' }}>grams/day</p>
                    </div>
                    <div>
                      <p style={{ fontSize: '0.875rem', color: '#718096', marginBottom: '0.25rem' }}>Carbs</p>
                      <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#d69e2e' }}>{user.carbsGoal}</p>
                      <p style={{ fontSize: '0.75rem', color: '#718096' }}>grams/day</p>
                    </div>
                    <div>
                      <p style={{ fontSize: '0.875rem', color: '#718096', marginBottom: '0.25rem' }}>Fats</p>
                      <p style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#e53e3e' }}>{user.fatsGoal}</p>
                      <p style={{ fontSize: '0.75rem', color: '#718096' }}>grams/day</p>
                    </div>
                  </div>
                  <div style={{ marginTop: '1rem', paddingTop: '1rem', borderTop: '1px solid #e2e8f0' }}>
                    <p style={{ fontSize: '0.875rem', color: '#718096' }}>
                      <strong>Macronutrient Distribution:</strong>
                    </p>
                    <p style={{ fontSize: '0.875rem', color: '#718096', marginTop: '0.5rem' }}>
                      Protein: {Math.round(user.proteinGoal * 4 / user.calorieGoal * 100)}% •
                      Carbs: {Math.round(user.carbsGoal * 4 / user.calorieGoal * 100)}% •
                      Fats: {Math.round(user.fatsGoal * 9 / user.calorieGoal * 100)}%
                    </p>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Calendar Section */}
      <div style={{ backgroundColor: 'white', padding: '2rem', borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
          <h2 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Meal Plan Calendar</h2>
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <button onClick={previousMonth} style={{ padding: '0.5rem 1rem', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}>
              ←
            </button>
            <span style={{ fontSize: '1.125rem', fontWeight: '600' }}>
              {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
            </span>
            <button onClick={nextMonth} style={{ padding: '0.5rem 1rem', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }}>
              →
            </button>
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: '0.5rem' }}>
          {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(day => (
            <div key={day} style={{ textAlign: 'center', fontWeight: '600', padding: '0.5rem', backgroundColor: '#f7fafc' }}>
              {day}
            </div>
          ))}
          {getDaysInMonth(currentMonth).map((day, index) => (
            <div
              key={index}
              style={{
                minHeight: '80px',
                padding: '0.5rem',
                backgroundColor: day ? (hasMealPlan(day) ? '#d6f5d6' : 'white') : '#f7fafc',
                border: '1px solid #e2e8f0',
                borderRadius: '6px',
                textAlign: 'center'
              }}
            >
              {day && (
                <>
                  <div style={{ fontWeight: '600', marginBottom: '0.5rem' }}>{day}</div>
                  {hasMealPlan(day) && <div style={{ fontSize: '1.5rem' }}>🍽️</div>}
                </>
              )}
            </div>
          ))}
        </div>

        <div style={{ marginTop: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <div style={{ width: '20px', height: '20px', backgroundColor: '#d6f5d6', border: '1px solid #68d391', borderRadius: '4px' }}></div>
          <span>Meal plan scheduled</span>
        </div>
      </div>
    </div>
  );
}

export default Profile;
